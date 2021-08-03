package cn.com.bbut.iy.itemmaster.serviceimpl.audit;

import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.dao.Ma1105Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000TMapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010TMapper;
import cn.com.bbut.iy.itemmaster.dao.audit.AuditBeanMapper;
import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1105.Ma1104Dto;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Example;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import cn.com.bbut.iy.itemmaster.service.order.OrderDcUrgentService;
import cn.com.bbut.iy.itemmaster.service.order.OrderVendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
//@Transactional
public class AuditServiceImpl implements IAuditService {
    @Autowired
    private AuditBeanMapper auditBeanMapper;
    @Autowired
    protected IReviewService reviewServiceImpl;
    @Autowired
    private OrderDcUrgentService orderDcUrgentService;
    @Autowired
    private OD0000Mapper od0000Mapper;
    @Autowired
    private OD0000TMapper od0000TMapper;
    @Autowired
    private OD0010TMapper od0010TMapper;
    @Autowired
    private Ma1105Mapper ma1105Mapper;
    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 根据主键查询
     */
    @Override
    public AuditBean getAuditById(long nRecordid) {
        return auditBeanMapper.selectAuditById(nRecordid);
    }

    /**
     * 根据主档ID和适用开始日获取记录
     */
    @Override
    public List<AuditBean> getAuditByRecordIdAndTime(AuditBean audit) {
        return auditBeanMapper.selectAuditByRecordIdAndTime(audit);
    }

    /**
     * 获取主键最大值
     */
    @Override
    public int getMaxId() {
        return auditBeanMapper.selectMaxId();
    }

    /**
     * 根据主档ID和适用开始日获取操作序列
     */
    @Override
    public int getMaxSubNo(AuditBean audit) {
        return auditBeanMapper.selectMaxSubNo(audit);
    }

    /**
     * 添加记录
     */
    @Override
    public int addRecord(AuditBean audit) {
        return auditBeanMapper.insertAudit(audit);
    }

    /**
     * 更新审核信息
     */
    @Override
    public int updateAuditInfo(AuditBean audit) {
        return auditBeanMapper.updateAuditInfo(audit);
    }

    /**
     * 根据主档ID和适用开始日设置记录无效
     */
    @Override
    public int updateVoidAudit(AuditBean audit) {
        return auditBeanMapper.invalidAudit(audit);
    }

    /**
     * 根据主档ID、适用开始日和操作序列获取记录
     */
    @Override
    public AuditBean getAuditByRecordIdAndTimeAndSubNo(int nTypeid, String recordCd, String startDate) {
        return auditBeanMapper.selectAuditByRecordIdAndTimeAndSubNo(nTypeid, recordCd, startDate);
    }

    /**
     * 根据主档ID和操作序列查询记录
     */
    @Override
    public AuditBean getIdByRecordId(int nTypeid, String recordCd) {
        return auditBeanMapper.selectByRecordId(nTypeid, recordCd);
    }

    /**
     * 根据编号查询是否有进行审核
     */
    @Override
    public int getInAuditById(String distinguishId, String recordCd,String effectiveStartDate) {
        log.debug("getInAuditById()---start");
        return auditBeanMapper.selectInAuditById(distinguishId, recordCd,effectiveStartDate);
    }

    /**
     * 根据类型编号查询类别编号
     */
    @Override
    public String getDistinguishById(int typeId) {
        return auditBeanMapper.selectDistinguishById(typeId);
    }

    /**
     * 更新流程第一条步骤的状态值
     */
    @Override
    public int updateAuditStatus(AuditBean audit) {
        return auditBeanMapper.updateAuditStatus(audit);
    }

    /**
     * 审核修改表状态
     * @param table 表名
     * @param key 数据字段名
     * @param recordCd 数据cd
     * @param status 审核状态值
     * @param reviewId 审核流程id
     * @param userId 审核用户id
     * @return
     */
    @Override
    public int updateRecordStatus(String table,String key,String recordCd, int status,long reviewId,String userId) {
        int flg = 0;
        try {
            String date = null;
            String time = null;
            log.error("reviewId:"+reviewId+"  table:"+table+"  recordCd:"+recordCd+"------------------------");
            String nowDate = ma4320Service.getNowDate();
            String ymd = nowDate.substring(0,8);
            String hms = nowDate.substring(8,14);
            String mouth = "";
            OD0000TDTO _dto = new OD0000TDTO();
            if(status==10){
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                // 当前时间年月日
                date = ymd;
                // 当前时间时分秒
                time = hms;
                /*
                折扣针对向供应商第一次订货  dc没有
                if((int)reviewId==ConstantsAudit.REVIEW_ORDER_DC){//dc订货审核通过
                    //更新订货折扣金额
                    orderDcUrgentService.updateDiscount(recordCd,date);
                }
                */
                log.error("reviewId:"+reviewId+"--------nowDate:"+nowDate);
                //收货审核通过
                if((int)reviewId==ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE
                    ||(int)reviewId==ConstantsAudit.REVIEW_RECEIPT_VENDOR){
                    //收货更新订货主档收货信息
                    _dto.setReceiveId(recordCd);
                    CommonDTO commonDTO = new CommonDTO();
                    commonDTO.setUpdateUserId(userId);
                    commonDTO.setUpdateYmd(date);
                    commonDTO.setUpdateHms(time);
                    _dto.setCommonDTO(commonDTO);
                    _dto.setOrderSts("04");
                    _dto.setReviewSts(20);
                    OD0000TDTO getOd0000_t = od0000TMapper.selectByOrder(recordCd);
                    log.error("getOd0000_t:"+getOd0000_t);
                    mouth = getOd0000_t.getPhysicalReceivingDate().substring(4,6);
                    od0000TMapper.updateOldRecord(_dto);
                    od0010TMapper.updateOldDetails(_dto);
                }else if((int)reviewId==ConstantsAudit.REVIEW_RETURN_ENTRY_WAREHOUSE
                    ||(int)reviewId==ConstantsAudit.REVIEW_RETURN_ENTRY_VENDOR){
                    OD0000 dto = new OD0000();
                    //添加实际退货日期
                    dto.setReceiveDate(date);
                    //设置退货状态为已收货
                    dto.setOrderSts("04");
                    //设置为已退货确认
                    dto.setReviewStatus(20);
                    log.error("reviewId:"+reviewId+"--------退货审核recordCd:"+recordCd);
                    OD0000Example example = new OD0000Example();
                    example.or().andOrderIdEqualTo(recordCd);
                    od0000Mapper.updateByExampleSelective(dto,example);
                }else if((int)reviewId==ConstantsAudit.REVIEW_REC_RET_CORRECTION){
                    OD0000 dto = new OD0000();
                    //添加实际收退货调整日期
                    dto.setReceiveDate(date);
                    OD0000Example example = new OD0000Example();
                    example.or().andOrderIdEqualTo(recordCd);
                    od0000Mapper.updateByExampleSelective(dto,example);
                }
            }
            else {
               log.error("status:"+status);
            }
            flg = auditBeanMapper.modifyRecordStatus(table, key, recordCd, status, reviewId, userId, date, time);

            if(((int)reviewId==ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE
                    ||(int)reviewId==ConstantsAudit.REVIEW_RECEIPT_VENDOR) && status==10){
                log.error("mouth:"+mouth);
                if(!date.substring(4,6).equals(mouth)){
                    _dto.setCheckMouthFlg(0);
                }else {
                    _dto.setCheckMouthFlg(1);
                }
                // 若实际收货时间与审核时间月份不同，更新收货时间月份为审核月份1号，06：00
                od0000TMapper.updateNewRecord(recordCd,_dto.getCommonDTO(),_dto.getCheckMouthFlg());
            }
            if("ma1106".equals(table)){
                // 获取POG的信息
                List<Ma1104Dto> pogInfos = ma1105Mapper.getPOGInformation(recordCd);
                for(Ma1104Dto pogInfo:pogInfos){
                    pogInfo.setReviewStatus(new BigDecimal(status));
                    if(status == 10){
                        pogInfo.setIsExpired("0");
                    }else {
                        pogInfo.setIsExpired("1"); // 失效的
                    }

                    // 更新ma1104的状态
                    ma1105Mapper.updatePOGAuditStatus(pogInfo);
                    String firstName = pogInfo.getPogName();
                    log.error("<<<<<<<pogInfo:"+pogInfo);
                    if(status == 10){
                        String[] arr = pogInfo.getPogName().split("-");
                        String pogName =  arr[0];
                        pogInfo.setPogName(pogName);
                        // 使之前上传货架的状态失效
                        ma1105Mapper.updatePOGIsExpiredStatus(pogInfo);
    //                    pogInfo.setPogName(firstName);
                        List<Ma1105> getExamples = ma1105Mapper.getTempShelf(recordCd,pogName);
                        if(getExamples.size()>0){
                            //删除已有货架的数据
                            ma1105Mapper.deleteMa1105Byshelf(getExamples,pogName);
                        }
                    }
                }
                if(status == 10) {
                    // 导入数据
                    ma1105Mapper.insertShelfToMall05(recordCd);
                }

                if(status != 1){
                    ma1105Mapper.deleteShelf(recordCd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flg = -1;
            log.error("AuditServiceImpl.modifyRecordStatus()-----"+e.getMessage());
        }
        return flg;
    }

    @Override
    public Integer getAuditStatus(String table, String key, String recordCd) {
        if("od0000".equals(table) && "order_id".equals(key)){
            return auditBeanMapper.getOrderAuditStatus(table,key,recordCd);
        }
        return auditBeanMapper.getAuditStatus(table,key,recordCd);
    }

    @Override
    public int updateRecordStatus(long auditStepId, String auditStatus,String userId) {
        int count;
        // 获取最新审核步骤
        AuditBean auditStep = this.getAuditById(auditStepId);
        if(auditStep == null){
            count = -1;
        }
        long reviewid = auditStep.getNReviewid();
        String recordCd = auditStep.getCRecordCd();
        int typeId = auditStep.getNTypeid();

        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            count = -1;
        }
        if(auditStatus.equals("1")){
            // 判断是否已Completed
            long subMax = reviewServiceImpl.getSubMaxByReviewId(reviewid);
            long nowSubNo = auditStep.getSubNo();
            if(subMax == nowSubNo){
                // 记录Completed
                count = this.updateRecordStatus(table,key, recordCd, 10,reviewid,userId);
            }else{
                // 记录审核流程未完成
                count = 1;
            }
        }else if(auditStatus.equals("0")){
            // 记录未通过审核
            count = this.updateRecordStatus(table,key,recordCd, 5,0,null);
        }else{
            count = -1;
        }
        return count;
    }

    @Override
    public int checkAuditByUserId(String storeCd, String userId, long position) {
        int auditNum = 0;
        /*if(4==position){ //店铺管理员
            auditNum = auditBeanMapper.checkAuditByUserId(storeCd,userId);
        }else */
        if(userId.equals("system")||userId.equals("nri")||userId.equals("demo001")||userId.equals("demo002")){
            auditNum = auditBeanMapper.checkAuditByUserIdAndPosition(storeCd,userId,position) ;
//        }else if(position>=4){ //除去 om am oc    含sm
//            auditNum = auditBeanMapper.checkAuditByPosition(storeCd,userId,position);
        }else{//om am oc
            auditNum = auditBeanMapper.checkAuditByUserIdAndPosition(storeCd,userId,position) ;
        }
        return auditNum;
    }

    @Override
    public String getCorrType(String reTableName, String recordCd,Integer typeId) {
        return auditBeanMapper.getCorrType(reTableName,recordCd,typeId);
    }
}
