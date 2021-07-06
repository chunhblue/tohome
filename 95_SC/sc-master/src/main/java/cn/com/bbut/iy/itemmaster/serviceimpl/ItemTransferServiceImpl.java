package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.Cm9010Mapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.SK0010Mapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.ItemTransferService;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.serviceimpl.inventory.InventoryVouchersServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ItemTransferServiceImpl implements ItemTransferService {

    @Autowired
    private InventoryVouchersMapper inventoryVouchersMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private Cm9010Mapper cm9010Mapper;
    @Autowired
    private SequenceService sequenceService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;
    @Autowired
    private SK0010Mapper sK0010Mapper;

    /*
     * 类型&条件查询数据
     *
     */
    @Override
    public GridDataDTO<InventoryVouchersGridDTO> getByTypeCondition(InventoryVouchersParamDTO param) {

        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        int count = inventoryVouchersMapper.selectCountItemTransfer(param);
        if(count < 1){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }

        List<InventoryVouchersGridDTO> _list = inventoryVouchersMapper.selectItemTransfer(param);

        GridDataDTO<InventoryVouchersGridDTO> data = new GridDataDTO<InventoryVouchersGridDTO>(_list,
                param.getPage(), count, param.getRows());

        return data;
    }

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 自动加载下拉
     * @param v
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getTypeList(String v) {
        return cm9010Mapper.getTypeList(v);
    }

    @Override
    @Transactional
    public String insert(Sk0010DTO sk0010, List<Sk0020DTO> sk0020List) {
        String _id = "";
        try{
            if ("602".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_ist_id_seq","IST",sk0010.getStoreCd());
            } else if ("601".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_ist_id_seq","IST",sk0010.getStoreCd());
            }
            if(StringUtils.isBlank(_id)){
                throw new RuntimeException("生成传票编号失败");
            }
            _id = _id.substring(0,10)+(Integer.parseInt(_id.substring(10, 14)) - 1) +_id.substring(14);
            sk0010.setVoucherNo(_id);

            // 是否上传初始为No
            sk0010.setUploadFlg("0");
            inventoryVouchersMapper.insertSk0010(sk0010);
            log.error("voucherType:"+sk0010.getVoucherType()+"<br>"+"sk0010:"+sk0010);
            // 保存明细
            for(Sk0020DTO bean : sk0020List){
                bean.setVoucherNo(sk0010.getVoucherNo());
                bean.setCommonDTO(sk0010.getCommonDTO());
                bean.setUploadFlg("0");
                inventoryVouchersMapper.insertSk0020(bean);
            }
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return null;
        }
        return _id;
    }

    /**
     * 修改传票
     *
     * @param sk0010
     * @param sk0020List
     */
    @Override
    @Transactional
    public int update(SK0010 sk0010, List<Sk0020DTO> sk0020List) {
        int i = 0;
        try {
            // 保存头档
            sK0010Mapper.updateByPrimaryKeySelective(sk0010);
            // 保存明细
            inventoryVouchersMapper.deleteSk0020ByKey(sk0020List.get(0));
            for(Sk0020DTO bean : sk0020List){
                bean.setUploadFlg("0");
                inventoryVouchersMapper.insertSk0020(bean);
            }

        }catch (RuntimeException e){
            log.error(e.getMessage());
            return 0;
        }
        return i;
    }

    /**
     * 查询详情数据
     *
     * @param sk0020
     */
    @Override
    public GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020) {
        String inEsTime = cm9060Service.getValByKey("1206");
        GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
        List<Sk0020DTO> _list = inventoryVouchersMapper.InstoreListSk0020(sk0020);
        if(_list == null || _list.equals("")){
            log.info("<<<<<<<<<<_list is null");
        }else {
            for (Sk0020DTO sk02: _list) {
                // 获取实时库存
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + sk02.getStoreCd() + "/"
                        + sk02.getArticleId() + "/*/*/*/*/" + inEsTime+"/*/*";
                RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
                BigDecimal inventoryQty = rTInventoryQueryDTO.getRealtimeQty();
                if(inventoryQty == null){
                    sk02.setInventoryQty("0");
                }else {
                    sk02.setInventoryQty(inventoryQty.toString());
                }
            }
        }
        data.setRows(_list);
        return data;
    }
}
