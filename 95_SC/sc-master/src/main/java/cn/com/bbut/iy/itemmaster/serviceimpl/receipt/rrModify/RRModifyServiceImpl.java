package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.rrModify;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010Example;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.receipt.rrModify.IRRModifyService;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 进退货传票修正实现
 *
 */
@Service
@Slf4j
public class RRModifyServiceImpl implements IRRModifyService {

    @Autowired
    private RRModifyMapper rrMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private OD0010Mapper od0010Mapper;
    @Autowired
    private OD0000TMapper od0000TMapper;
    @Autowired
    private OD0010TMapper od0010TMapper;
    @Autowired
    private SequenceService sequenceService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;

    @Autowired
    private CM9060Service cm9060Service;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        // 数据从总部系统迁移，业务日期需匹配
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询记录
     *
     * @param param
     */
    @Override
    public GridDataDTO<RRModifyDTO> getList(RRModifyParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        int count = rrMapper.selectCountByCondition(param);
        if(count < 1){
            return new GridDataDTO<RRModifyDTO>();
        }

        List<RRModifyDTO> _list = rrMapper.selectListByCondition(param);

        GridDataDTO<RRModifyDTO> data = new GridDataDTO<RRModifyDTO>(_list,
                param.getPage(), count, param.getRows());

        return data;
    }

    /**
     * 主键查询头档
     *
     * @param param
     */
    @Override
    public RRModifyDTO getRecordByKey(RRModifyParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        RRModifyDTO _dto = null;
        switch(param.getOrderType()){ // 07:收货修正，08:退货修正
            case "07":
                _dto = rrMapper.selectReceiveByKey(param);
                break;
            case "08":
                _dto = rrMapper.selectReturnByKey(param);
                break;
            default:
                _dto = rrMapper.selectByKey(param);
        }
        return _dto;
    }

    /**
     * 查询明细详情
     *
     * @param param
     */
    @Override
    public GridDataDTO<RRModifyDetailsDTO> getDetail(RRModifyParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        GridDataDTO<RRModifyDetailsDTO> _return = new GridDataDTO<RRModifyDetailsDTO>();
        List<RRModifyDetailsDTO> _list = null;
        int count = 0;
        switch(param.getOrderType()){
            case "07":
                _list = od0010TMapper.selectReceiveModify(param);
                count = od0010TMapper.selectCountReceiveModify(param);
                break;
            case "08":
                _list = rrMapper.selectReturnModify(param);
                count = rrMapper.selectCountReturnModify(param);
                break;
            default:
                _list = rrMapper.selectDetailsByKey(param);
                count = rrMapper.selectCountDetailsByKey(param);
        }
        List<String> articleIdList = new ArrayList<>();
        if(_list.size()>0){
            for(RRModifyDetailsDTO rtDto:_list){
                articleIdList.add(rtDto.getArticleId());
                if(BigDecimal.ZERO.equals(rtDto.getModifyQty()) || rtDto.getModifyQty() == null){
                    rtDto.setModifyQty(rtDto.getRrOrderQty());
                }
            }
        }
        try {
            // List转jackJosn字符串
            String articleIdListJson = new ObjectMapper().writeValueAsString(articleIdList);

            String inEsTime = cm9060Service.getValByKey("1206");
            String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+param.getStoreCd()
                    +"/*/*/*/*/*/" + inEsTime+"/*/*";
            String urlData = RealTimeInventoryQueryServiceImpl.RequestPost(articleIdListJson,connUrl);
            if(urlData == null || "".equals(urlData)){
                String message = "Failed to connect to live inventory data！";
            }
            Gson gson = new Gson();
            // 获取第一层的信息
            ArrayList<RtInvContent> rtInvContent2 = gson.fromJson(urlData,new TypeToken<List<RtInvContent>>() {}.getType());

            RtInvContent rtInvContent = rtInvContent2.get(0);
            if(rtInvContent == null){
                rtInvContent = new RtInvContent();
            }
            String content = rtInvContent.getContent();
            // 获取第二层的信息
            ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
            if(realTimeDto2.size()>0) {
                for (RRModifyDetailsDTO rtDto : _list) {
                    for (RealTimeDto realTimeDto : realTimeDto2) {
                        if (realTimeDto.getArticle_id().equals(rtDto.getArticleId())) {
                        // 计算实时库存数量
                            BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                    .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                    .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                    .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                            rtDto.setRealtimeQty(rTimeQty);
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        _return.setRows(_list);
        _return.setPage(param.getPage());
        _return.setRecords(count);
        _return.setRowPerPage(param.getRows());
        return _return;
    }

    @Override
    public RRModifyDTO getdirectItemInfo(RRModifyParamDTO dto) {
        String businessDate = getBusinessDate();
        RRModifyDTO rrDto = rrMapper.getdirectItemInfo(dto,businessDate);
        if(rrDto.getOnHandQty() == null){
            rrDto.setOnHandQty(BigDecimal.ZERO);
        }else if(rrDto.getOnOrderQty() == null) {
            rrDto.setOnOrderQty(BigDecimal.ZERO);
        }
        return rrDto;
    }

    /**
     * 保存记录
     */
    @Override
    @Transactional
    public String insertRecord(RRModifyDTO dto, List<OD0010> list) {
        // 生成修正单编号
        // String orderId = getOrderId(dto);
        // 获取业务日期
//        String businessDate = getBusinessDate();
//        dto.setOrderDate(businessDate);
        String orderId = null;
        try{
            // 设置订单终止日
//            SimpleDateFormat sj = new SimpleDateFormat("yyyyMMdd");
//            Date date = sj.parse(businessDate);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DATE, 1);
//            String newDate = sj.format(calendar.getTime());
//            dto.setExpireDate(newDate);
            // 更新头档信息
            String upFlg = "";
            if(StringUtils.isBlank(dto.getOrderId())){
                orderId = sequenceService.getSequence("od0000_crr_id_seq","CRR",dto.getStoreCd());
                upFlg = "add";
            }else{
                upFlg = "edit";
                orderId = dto.getOrderId();
            }
            if(orderId == null){
                return null;
            }
            List<OD0010> corrList = od0010Mapper.getOD0010List(orderId);
            if(corrList.size()>0){
                for(int i=0;i<corrList.size();i++){
                    for(int n=0;n<list.size();n++){
                        if(list.get(n).getArticleId().equals(corrList.get(i).getArticleId())){
                            list.get(n).setLastCorrectionDifference(corrList.get(i).getLastCorrectionDifference());
                        }
                    }
                }
            }


            CommonDTO commonDTO = dto.getCommonDTO();
            if("add".equals(upFlg)){
                dto.setOrderId(orderId);
                rrMapper.insertRecord(dto);
            }else if ("edit".equals(upFlg)){
                rrMapper.updateRecord(dto);
                //删除明细数据
                OD0010Example example = new OD0010Example();
                example.or().andOrderIdEqualTo(orderId);
                od0010Mapper.deleteByExample(example);
            }
            // 保存明细数据
            for(OD0010 bean : list){
                bean.setOrderId(orderId);
                bean.setCreateUserId(commonDTO.getCreateUserId());
                bean.setCreateYmd(commonDTO.getCreateYmd());
                bean.setCreateHms(commonDTO.getCreateHms());
                bean.setUpdateUserId(commonDTO.getUpdateUserId());
                bean.setUpdateYmd(commonDTO.getUpdateYmd());
                bean.setUpdateHms(commonDTO.getUpdateHms());
                bean.setIsFreeItem("0"); // normal item
                od0010Mapper.insert(bean);
            }
//        }catch (ParseException e){
//            log.error("日期格式转换异常---"+e.getMessage());
//            return 0;
        }catch (RuntimeException e){
            log.error("传票保存异常---"+e.getMessage());
            return null;
        }
        return orderId;
    }

    /**
     * 获取原订单待选list
     *
     * @param type
     * @param storeCd
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getOrgOrderId(String type, String storeCd, String v) {
        List<AutoCompleteDTO> _list = null;
        switch (type){
            case "07":
                _list = od0000TMapper.selectOrgOrderId(storeCd, v);
                break;
            case "08":
                _list = rrMapper.selectOrgOrderId(storeCd, v);
                break;
        }
        if(_list == null || _list.size() == 0){
            return _list;
        }
        Collection<String> receiveIds = new ArrayList<>();
        for(AutoCompleteDTO  dto : _list){
            receiveIds.add(dto.getV());
        }
        List<OD0000> corrList = rrMapper.getCorrList(receiveIds);
        List<OD0000> newList = new ArrayList<>();
        for(OD0000 oldItem : corrList){
            boolean checkFlg = true;
            for(OD0000 newItem : newList){
                if(newItem.getOrgOrderId().equals(oldItem.getOrgOrderId())){
                    if(oldItem.getCreateYmd()!=null && oldItem.getCreateHms()!=null
                    && newItem.getCreateYmd()!=null&& newItem.getCreateHms()!=null){
                        if(Long.parseLong(oldItem.getCreateYmd()+oldItem.getCreateHms())
                                > Long.parseLong(newItem.getCreateYmd()+newItem.getCreateHms())){
                            newItem.setOrgOrderId(oldItem.getOrgOrderId());
                            newItem.setOrderId(oldItem.getOrderId());
                            newItem.setReviewStatus(oldItem.getReviewStatus());
                            newItem.setCreateYmd(oldItem.getCreateYmd());
                            newItem.setCreateHms(oldItem.getCreateHms());
                            newItem.setStoreCd(oldItem.getStoreCd());
                        }
                    }
                    checkFlg = false;
                }
            }
            if(checkFlg){
                newList.add(oldItem);
            }
        }
        if(newList.size()>0){
            for(int i =0;i<_list.size();i++){
                AutoCompleteDTO dto = _list.get(i);
                for(OD0000 newItem : newList){
                    if(dto.getV().equals(newItem.getOrgOrderId()) && newItem.getReviewStatus() != 10){
                        _list.remove(dto);
                        i--; // 索引减1，保证索引正常，不然报java.util.ConcurrentModificationException
                    }
                }
            }
        }

        return _list;
    }

    // 统计收货单号已进行退货流程的数量
    @Override
    public int countOrderId(String receiveId) {
        int count = od0000TMapper.orgOrderIdCount(receiveId);
        return count;
    }

    @Override
    public int updateLastCorr(String orderId) {
        int i = od0010Mapper.updateLastCorr(orderId);
        return i;
    }

    /**
     * 生成订单编号
     *
     * @param dto
     */
    public String getOrderId(RRModifyDTO dto) {
        String orderId = null;
        String orderType = dto.getOrderType();
        String storeCd = dto.getStoreCd();
        String ymd = dto.getCommonDTO().getCreateYmd();
        String hms = dto.getCommonDTO().getCreateHms();
        switch (orderType){
            case "07":
                orderId = "7" + storeCd + ymd.substring(2) + hms;
                break;
            case "08":
                orderId = "8" + storeCd + ymd.substring(2) + hms;
                break;
        }
        return orderId;
    }
}
