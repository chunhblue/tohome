package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000TMapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010TMapper;
import cn.com.bbut.iy.itemmaster.dao.receipt.warehouse.WarehouseMapper;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationBean;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.ReceiveService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mxy
 */
@Service
@Slf4j
public class ReceiveServiceImpl implements ReceiveService {
 
    @Autowired
    private OD0000TMapper od0000TMapper;
    @Autowired
    private OD0010TMapper od0010TMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    protected INotificationService notificationServiceImpl;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 根据订单号查询收货单
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<OD0000TDTO> getList(OD0000TParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        int count = od0000TMapper.selectCountByOrder(dto);
        if(count == 0){
            return new GridDataDTO<OD0000TDTO>();
        }
        List<OD0000TDTO> _list = od0000TMapper.selectListByOrder(dto);
        GridDataDTO<OD0000TDTO> data = new GridDataDTO<OD0000TDTO>(_list, dto.getPage(), count, dto.getRows());
        return data;
    }

    /**
     * 查询头档信息
     *
     * @param dto
     * @return
     */
    @Override
    public OD0000TDTO getOD0000(OD0000TParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);
        return od0000TMapper.selectByKey(dto);
    }

    /**
     * 查询商品明细信息
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<OD0010TDTO> getOD0010(OD0000TParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);
        GridDataDTO<OD0010TDTO> data = new GridDataDTO<OD0010TDTO>();
        List<OD0010TDTO> _list;
        if(dto.getOrderType().equals("0")){
            _list = od0010TMapper.selectDcDetail(dto);
        }else {
            _list = od0010TMapper.selectVendorDetail(dto);
        }
        data.setRows(_list);
        return data;
    }

    /**
     * 快速收货
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Map<String,String> insertByQuick(List<OD0000TDTO> list, CommonDTO dto) {
        int i = 0;
        StringBuilder recevieBuilder = new StringBuilder();
        StringBuilder storeCdBuilder = new StringBuilder();
        try{
            for(OD0000TDTO _dto : list){
                long reviewId = 0;
                int typeId = 0;
                _dto.setCommonDTO(dto);
                // 订货区分 "0"供应商  "1"物流
                String orderDiff = _dto.getOrderDifferentiate();
                String receiveId = "";
                if ("1".equals(orderDiff)) {
                    receiveId = sequenceService.getSequence("od0000_t_red_id_seq","RED",_dto.getStoreCd());
                    reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE));
                    typeId = ConstantsAudit.TYPE_RECEIPT_WAREHOUSE;
                } else if ("0".equals(orderDiff)) {
                    receiveId = sequenceService.getSequence("od0000_t_res_id_seq","RES",_dto.getStoreCd());
                    reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_VENDOR));
                    typeId = ConstantsAudit.TYPE_RECEIPT_VENDOR;
                }
                if(i == 0){
                    od0000TMapper.deleteCopy(_dto);
                    od0010TMapper.deleteCopy(_dto);
                }
                warehouseMapper.modifyReviewStatus(_dto.getOrderId());
                _dto.setReceiveId(receiveId);
                OD0000TDTO tdto = od0000TMapper.getHHTreceiveDateTime(_dto.getOrderId(),_dto.getStoreCd());
                if(tdto != null){
                    _dto.setCheckHhtFlg(0);
                    _dto.setPhysicalReceivingDate(tdto.getPhysicalReceivingDate());
                    _dto.setPhysicalReceivingHms(tdto.getPhysicalReceivingHms());
                }
                od0000TMapper.insertByCopy(_dto);
                if("0".equals(orderDiff)){
                    od0010TMapper.insertByCopy(_dto);
                } else if ("1".equals(orderDiff)) {
                    od0010TMapper.insertDCByCopy(_dto);
                }

                // 收货邮件发送
                NotificationBean notificationBean = new NotificationBean();
                // 店铺cd
                notificationBean.setStoreCd(_dto.getStoreCd());
                // 流程ID
                notificationBean.setNReviewid(reviewId);
                // 优先级
                notificationBean.setCPriority(91);
                // 步骤
                notificationBean.setCSubNo(1);
                // 类型ID
                notificationBean.setNTypeid(typeId);
                // 主档ID
                notificationBean.setCRecordCd(receiveId);
                // 角色ID
                notificationBean.setNRoleid(4);
                // URL
                String Url;
                if(typeId != 0){
                    Url = notificationServiceImpl.getUrlByTypeId(typeId);
                }else{
                    Url = "";
                }
                notificationBean.setCUrl(Url);
                //与邮件有关
                notificationServiceImpl.sendEmail(notificationBean);

                /*_dto.setOrderSts("04");
                _dto.setReviewSts(20);
                od0000TMapper.updateOldRecord(_dto);
                od0010TMapper.updateOldDetails(_dto);*/
                recevieBuilder.append(_dto.getReceiveId()+",");
                storeCdBuilder.append(_dto.getStoreCd()+",");
                i++;
            }
        }catch (Exception e){
            log.error("quickReceive >>>>>> "+e.getMessage());
            return null;
        }
        Map<String,String> map = new HashMap<>();
        map.put("receiveId",recevieBuilder.toString());
        map.put("storeCd",storeCdBuilder.toString());
        return map;
    }

    /**
     * 订单收货
     *
     * @param dto
     * @param list
     * @return
     */
    @Override
    @Transactional
    public String insertByReceive(OD0000TDTO dto, List<OD0010TDTO> list) {
        long reviewId = 0;
        int typeId = 0;
        // 订货区分
        String orderDiff = dto.getOrderDifferentiate();
        String receiveId = "";
        if ("1".equals(orderDiff)) {
            receiveId = sequenceService.getSequence("od0000_t_red_id_seq","RED",dto.getStoreCd());
            reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE));
            typeId = ConstantsAudit.TYPE_RECEIPT_WAREHOUSE;
        } else if ("0".equals(orderDiff)) {
            receiveId = sequenceService.getSequence("od0000_t_res_id_seq","RES",dto.getStoreCd());
            reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_VENDOR));
            typeId = ConstantsAudit.TYPE_RECEIPT_VENDOR;
        }
        try{
            dto.setReceiveId(receiveId);
            od0000TMapper.insertRecord(dto);
            warehouseMapper.modifyReviewStatus(dto.getOrderId());
            for(OD0010TDTO _dto : list){
                _dto.setReceiveId(receiveId);
                od0010TMapper.insertByKey(_dto);
            }
            // 收货邮件发送
            NotificationBean notificationBean = new NotificationBean();
            // 店铺cd
            notificationBean.setStoreCd(dto.getStoreCd());
            // 流程ID
            notificationBean.setNReviewid(reviewId);
            // 优先级
            notificationBean.setCPriority(91);
            // 步骤
            notificationBean.setCSubNo(1);
            // 类型ID
            notificationBean.setNTypeid(typeId);
            // 主档ID
            notificationBean.setCRecordCd(receiveId);
            // 角色ID
            notificationBean.setNRoleid(4);
            // URL
            String Url;
            if(typeId != 0){
                Url = notificationServiceImpl.getUrlByTypeId(typeId);
            }else{
                Url = "";
            }
            notificationBean.setCUrl(Url);
            //与邮件有关
            notificationServiceImpl.sendEmail(notificationBean);

            /*dto.setOrderSts("04");
            dto.setReviewSts(20);
            od0000TMapper.updateOldRecord(dto);
            od0010TMapper.updateOldDetails(dto);*/
            //添加附件信息
            if(StringUtils.isNotBlank(dto.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(dto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int i = 0; i < ma4320List.size(); i++) {
                    MA4320 ma4320 = ma4320List.get(i);
                    ma4320.setInformCd(receiveId);
                    ma4320.setCreateUserId(dto.getCommonDTO().getCreateUserId());
                    ma4320.setCreateYmd(dto.getCommonDTO().getCreateYmd());
                    ma4320.setCreateHms(dto.getCommonDTO().getCreateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }
        }catch (Exception e){
            log.error("receive >>>>>> "+e.getMessage());
            return null;
        }
        return receiveId;
    }

    /**
     * 收货单修改
     * @param dto
     * @param list
     * @return
     */
    @Override
    @Transactional
    public String updateReceive(OD0000TDTO dto, List<OD0010TDTO> list) {
        long reviewId = 0;
        int typeId = 0;
        String receiveId = dto.getReceiveId();
        if ("1".equals(dto.getOrderDifferentiate())) {
           reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE));
            typeId = ConstantsAudit.TYPE_RECEIPT_WAREHOUSE;
        } else if ("0".equals(dto.getOrderDifferentiate())) {
           reviewId = Long.parseLong(String.valueOf(ConstantsAudit.REVIEW_RECEIPT_VENDOR));
            typeId = ConstantsAudit.TYPE_RECEIPT_VENDOR;
        }
        try{
            od0010TMapper.deleteByReceiveId(receiveId);
            od0000TMapper.updateRecord(dto);
            warehouseMapper.modifyReviewStatus(dto.getOrderId());
            for(OD0010TDTO _dto : list){
                _dto.setReceiveId(receiveId);
                od0010TMapper.insertByKey(_dto);
            }
            // 收货邮件发送
            NotificationBean notificationBean = new NotificationBean();
            // 店铺cd
            notificationBean.setStoreCd(dto.getStoreCd());
            // 流程ID
            notificationBean.setNReviewid(reviewId);
            // 优先级
            notificationBean.setCPriority(91);
            // 步骤
            notificationBean.setCSubNo(1);
            // 类型ID
            notificationBean.setNTypeid(typeId);
            // 主档ID
            notificationBean.setCRecordCd(receiveId);
            // 角色ID
            notificationBean.setNRoleid(4);
            // URL
            String Url;
            if(typeId != 0){
                Url = notificationServiceImpl.getUrlByTypeId(typeId);
            }else{
                Url = "";
            }
            notificationBean.setCUrl(Url);
            //与邮件有关
            notificationServiceImpl.sendEmail(notificationBean);
            /*dto.setOrderSts("04");
            dto.setReviewSts(20);
            od0000TMapper.updateOldRecord(dto);
            od0010TMapper.updateOldDetails(dto);*/

            //添加附件信息
            MA4320Example example = new MA4320Example();
            example.or().andInformCdEqualTo(receiveId).andFileTypeEqualTo("03");
            ma4320Mapper.deleteByExample(example);
            if(StringUtils.isNotBlank(dto.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(dto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int i = 0; i < ma4320List.size(); i++) {
                    MA4320 ma4320 = ma4320List.get(i);
                    ma4320.setInformCd(receiveId);
                    ma4320.setCreateUserId(dto.getCommonDTO().getCreateUserId());
                    ma4320.setCreateYmd(dto.getCommonDTO().getCreateYmd());
                    ma4320.setCreateHms(dto.getCommonDTO().getCreateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }
        }catch (Exception e){
            log.error("receive >>>>>> "+e.getMessage());
            return null;
        }
        return receiveId;
    }

    @Override
    public Integer insertDocumentUrl(OD0000TDTO dto) {
        Integer countFil=0;
        String receiveId = dto.getReceiveId();
        MA4320Example example = new MA4320Example();
        example.or().andInformCdEqualTo(receiveId).andFileTypeEqualTo("03");
        ma4320Mapper.deleteByExample(example);
        if(StringUtils.isNotBlank(dto.getFileDetailJson())){
            List<MA4320> ma4320List = new Gson().fromJson(dto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
            for (int i = 0; i < ma4320List.size(); i++) {
                MA4320 ma4320 = ma4320List.get(i);
                ma4320.setInformCd(receiveId);
                ma4320.setCreateUserId(dto.getCommonDTO().getCreateUserId());
                ma4320.setCreateYmd(dto.getCommonDTO().getCreateYmd());
                ma4320.setCreateHms(dto.getCommonDTO().getCreateHms());
                countFil++;
                 ma4320Mapper.insertSelective(ma4320);
            }
            return  countFil;
        }else {
            return null  ;
        }
    }

    @Override
    public int updateStatus(String receiveId,CommonDTO dto) {
        return od0000TMapper.updateNewRecord(receiveId,dto,1);
    }

    @Override
    public boolean receiveIdIsExist(String orderId) {
        boolean flag  = false;
        OD0000TDTO dto = od0000TMapper.receiveIdIsExist(orderId);
        if (null != dto){
            flag = true;
        }
        return flag;
    }

    /**
     * 获取收货单编号
     */
    public String getReceiveId() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        String date = dateFormat.format(now);
        return date;
    }
}