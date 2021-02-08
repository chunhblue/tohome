package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.SA0070Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceSts;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SA0070Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class SA0070ServiceImpl implements SA0070Service {
    @Autowired
    private SA0070Mapper sa0070Mapper;
    @Autowired
    private Cm9070Service cm9070ServiceImpl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public String getArticleId(String barcode) {
        return sa0070Mapper.getArticleId(barcode);
    }

    @Override
    public PriceSts getPriceSts(String articleId) {
        return sa0070Mapper.getPriceSts(articleId);
    }

    @Override
    public String getChangeId(String changeId) {
        return sa0070Mapper.getChangeId(changeId);
    }

    @Override
    public Price getPrice(String articleId, String effectiveDate, String storeCd) {
        Price price = sa0070Mapper.getPrice(articleId,effectiveDate);
        BigDecimal newPrice = sa0070Mapper.getNewPrice(articleId,effectiveDate,storeCd);
        if(price!=null&&newPrice!=null)
            price.setOldPrice(newPrice);
        return price;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnDTO insertChangePrice(HttpSession session, HttpServletRequest request, SA0070 param, String orderDetailJson) {
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
                    //获取序列号
                    String deltaId = sequenceService.getSequence("sa0070_change_delta_id_seq");
                    if(StringUtils.isBlank(deltaId)){
                        //获取序列失败
                        log.error("获取序列失败 getSequence: {}", "sa0070_change_delta_id_seq");
                        RuntimeException e = new RuntimeException("获取序列失败[ sa0070_change_delta_id_seq ]");
                        throw e;
                    }

                    //添加紧急变价信息
                    SA0070 sa0070 = sa0070List.get(i);
                    //设置变价单号
                    if(StringUtils.isBlank(sa0070.getChangeId())){
                        sa0070.setChangeId(param.getChangeId());
                    }
                    //设置序列号
                    sa0070.setDeltaSerialNo(Integer.parseInt(deltaId));
                    sa0070.setCreateUserId(param.getCreateUserId());
                    sa0070.setCreateYmd(param.getCreateYmd());
                    sa0070.setCreateHms(param.getCreateHms());
                    sa0070Mapper.insertSelective(sa0070);
                }
                return new ReturnDTO(true,null,changeId);
            }
        }
        return new ReturnDTO(false);
    }

    @Override
    public String getArticleName(String articleId) {
        //获取业务时间
        String businessDate = cm9060Service.getValByKey("0000");
        return sa0070Mapper.getArticleName(articleId,businessDate);
    }

    @Override
    public GridDataDTO<PriceDetailGridDto> getList(PriceDetailParamDto param) {
        List<PriceDetailGridDto> list = sa0070Mapper.getList(param);
        list.stream().forEach(priceDetailGridDto -> {
            priceDetailGridDto.setCreateDate(priceDetailGridDto.getCreateYmd()+priceDetailGridDto.getCreateHms());
        });
        long count = sa0070Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public List<Price> getArticleAndName(String v, String storeCd, String effectiveDate) {
        effectiveDate=formatDate(effectiveDate);
        List<Price> prices = sa0070Mapper.selectArticleIdAndName(v,storeCd,effectiveDate);
        return prices;
    }

    // 25/05/2020 -> 20200525
    public String formatDate(String str) {
        List<String> list = new ArrayList<String>();
        if (str.indexOf("/")!=-1) {
            String[] strs = str.split("/");
            list = CollectionUtils.arrayToList(strs);
            Collections.reverse(list);
        }
        StringBuffer sb = new StringBuffer();
        list.forEach(item -> sb.append(item));
        return sb.toString();
    }
}
