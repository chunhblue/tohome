package cn.com.bbut.iy.itemmaster.serviceimpl.suspendSale;

import cn.com.bbut.iy.itemmaster.dao.SA0070Mapper;
import cn.com.bbut.iy.itemmaster.dao.SuspendSaleMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.suspendSale.SuspendSaleService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SuspendSaleServiceImpl implements SuspendSaleService {

    @Autowired
    private SA0070Mapper sa0070Mapper;

    @Autowired
    private SuspendSaleMapper suspendSaleMapper;

    @Autowired
    private Cm9070Service cm9070ServiceImpl;

    @Autowired
    private SequenceService sequenceService;

    /**
     * 查询商品基本信息
     */
    @Override
    public Price getPrice(String articleId, String effectiveDate) {
        Price price = sa0070Mapper.getPrice(articleId,effectiveDate);
        return price;
    }

    @Override
    public String getBarcodeByInfo(String articleId, String storeCd, String businessDate) {
        return sa0070Mapper.getBarcodeByInfo(articleId,storeCd,businessDate);
    }

    /**
     * 保存暂停销售商品信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSuppendSaleItem(HttpSession session, HttpServletRequest request, SA0070 param, String orderDetailJson) {
        if(StringUtils.isNotBlank(orderDetailJson)){
            List<SA0070> sa0070List = new Gson().fromJson(orderDetailJson, new TypeToken<List<SA0070>>(){}.getType());
            if(sa0070List!=null){
                //获取序列号
                String changeId = sequenceService.getSequence("sa0070_change_id_seq");
                if(StringUtils.isBlank(changeId)){
                    //获取序列失败
                    log.error("获取序列失败 getSequence: {}", "sa0070_change_id_seq");
                    RuntimeException e = new RuntimeException("获取序列失败[ sa0070_change_id_seq ]");
                    throw e;
                }
                //设置变价单号
                if(StringUtils.isBlank(param.getChangeId())){
                    param.setChangeId(changeId);
                }
                for (int i = 0; i < sa0070List.size(); i++) {
                    /* 自动采番开始 */
                    Cm9070ReturnDTO _cm9070ReturnDto = new Cm9070ReturnDTO();
                    try {
                        _cm9070ReturnDto = cm9070ServiceImpl.Get(session, request,
                                "SA0220", "temp_supsend_sale_delta_id", new Date(), param.getStoreCd());
                    } catch (UnknownHostException e) {
                        RuntimeException re = new RuntimeException("采番失败", e);
                        throw re;
                    }
                    if (0 != _cm9070ReturnDto.getCode()) {
                        log.error("采番失败", _cm9070ReturnDto.getMsg());
                        RuntimeException e = new RuntimeException("采番失败[ "
                                + _cm9070ReturnDto.getMsg() + " ]");
                        throw e;
                    }
                    /* 自动采番结束 */
                    //添加紧急变价信息
                    SA0070 sa0070 = sa0070List.get(i);
                    //设置变价单号
                    if(StringUtils.isBlank(sa0070.getChangeId())){
                        sa0070.setChangeId(param.getChangeId());
                    }
                    sa0070.setDeltaSerialNo(Integer.parseInt(_cm9070ReturnDto.getaSreturnNumber()));
                    sa0070.setCreateUserId(param.getCreateUserId());
                    sa0070.setCreateYmd(param.getCreateYmd());
                    sa0070.setCreateHms(param.getCreateHms());
                    suspendSaleMapper.insertSuppendSaleItem(sa0070);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 校验id是否冲突
     */
    @Override
    public String getDocId(String docId) {
        return suspendSaleMapper.getDocId(docId);
    }


    /**
     * 查询一览数据
     */
    @Override
    public GridDataDTO<PriceDetailGridDto> getList(PriceDetailParamDto param) {
        List<PriceDetailGridDto> list = suspendSaleMapper.getList(param);
        list.forEach(priceDetailGridDto -> {
            priceDetailGridDto.setCreateDate(priceDetailGridDto.getCreateYmd()+priceDetailGridDto.getCreateHms());
        });
        long count = suspendSaleMapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    /**
     * 获取组织架构的数据
     */
    @Override
    public List<AutoCompleteDTO> selectListByLevel(String level, String adminId, String articleId, String accDate,String v) {

        List<AutoCompleteDTO> list = null;

        if ("0".equals(level)) {
            list = suspendSaleMapper.selectListByLevel0(level,adminId,articleId,accDate,v);
        } else if ("1".equals(level)) {
            list = suspendSaleMapper.selectListByLevel1(level,adminId,articleId,accDate,v);
        } else if ("2".equals(level)) {
            list = suspendSaleMapper.selectListByLevel2(level,adminId,articleId,accDate,v);
        } else if ("3".equals(level)){
            list = suspendSaleMapper.selectListByLevel3(level,adminId,articleId,accDate,v);
        }

        return list;
    }

    /**
     * 根据区域cd获取下面所有的店铺
     */
    @Override
    public List<AutoCompleteDTO> getStoreListByDistrictCd(String cityCd,String districtCd, String articleId, String accDate) {
        List<AutoCompleteDTO> list = suspendSaleMapper.getStoreListByDistrictCd(cityCd,districtCd,articleId,accDate);
        return list;
    }

}
