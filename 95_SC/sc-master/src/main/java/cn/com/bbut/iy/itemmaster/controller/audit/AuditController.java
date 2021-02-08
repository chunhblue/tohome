package cn.com.bbut.iy.itemmaster.controller.audit;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dao.CustEntryMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.audit.*;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import cn.com.bbut.iy.itemmaster.service.inventoryQtyCor.AllRtInCorrQtyService;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 审核
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/audit")
public class AuditController extends BaseAction {
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    protected IReviewService reviewServiceImpl;
    @Autowired
    protected INotificationService notificationServiceImpl;
    @Autowired
    private AllRtInCorrQtyService allRtService;
    @Autowired
    private RealTimeInventoryQueryService rtService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1200Service ma1200Service;
    @Autowired
    private CustEntryMapper custEntryMapper;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;

    /**
     * 保存审核信息
     * @param typeId 审核类型
     * @param nReviewid 流程id
     * @param recordCd 数据cd
     * @param storeCd 店铺cd
     * @return
     */
    @RequestMapping("/saveAudit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto saveReview(int typeId, String nReviewid, String recordCd, String storeCd,
                                    HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        if(StringUtils.isBlank(storeCd)){
            storeCd = "000000";
        }
        if(typeId == 0 || StringUtils.isBlank(nReviewid)
                || StringUtils.isBlank(recordCd)
                || StringUtils.isBlank(storeCd)
            ){
            ard.setMessage("Parameters can not be empty!");
            ard.setSuccess(false);
            return ard;
        }

        long reviewId;
        try {
            reviewId = Long.parseLong(nReviewid);
        } catch (Exception e) {
            log.debug("参数错误 nReviewid {}",nReviewid);
            ard.setMessage("参数错误!");
            ard.setSuccess(false);
            return ard;
        }
        //获取审核类型对应信息
        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            ard.setSuccess(false);
            ard.setMessage("Failed to get information for audit type!");
            return ard;
        }

        String[] recordCds = recordCd.split(",");
        String[] storeCds = storeCd.split(",");
        if(recordCds.length>=1){
            for (int i = 0; i < recordCds.length; i++) {
                String rCd = recordCds[i];
                String storeCd1 = storeCds[i];
                if(StringUtils.isBlank(storeCd1)){
                    storeCd1 = "000000";
                }
                //修改主档审核状态和流程
                int updateRecordFlg = auditServiceImpl.updateRecordStatus(table,key,rCd, 1,reviewId,null);
                if(updateRecordFlg<0){
                    ard.setSuccess(false);
                    ard.setMessage("Modify master file audit status and process failure!");
                    return ard;
                }

                AuditBean auditBean = new AuditBean();
                AuditBean startAuditBean = new AuditBean();
                NotificationBean notificationBean = new NotificationBean();
                // 存入提交流程用户信息
                auditBean.setNOperatorid("");
                startAuditBean.setNOperatorid(u.getUserId());
                // 流程ID
                auditBean.setNReviewid(reviewId);
                startAuditBean.setNReviewid(reviewId);
                // 主档类型
                auditBean.setNTypeid(typeId);
                startAuditBean.setNTypeid(typeId);
                notificationBean.setNTypeid(typeId);
                // 主档ID
                auditBean.setCRecordCd(rCd);
                startAuditBean.setCRecordCd(rCd);
                notificationBean.setCRecordCd(rCd);
                // 适用开始日
                auditBean.setStoreCd(storeCd1);
                startAuditBean.setStoreCd(storeCd1);
                notificationBean.setStoreCd(storeCd1);

                auditBean.setEffectiveStartDate(storeCd1);
                startAuditBean.setEffectiveStartDate(storeCd1);
                notificationBean.setEffectiveStartDate(storeCd1);

                // 先将之前的流程设为无效
                log.debug("-------------------------将该记录之前的流程信息无效-------------------------");
                auditServiceImpl.updateVoidAudit(auditBean);
                log.debug("-------------------------将该记录的审核通知无效-------------------------");
                notificationServiceImpl.updateVoidNotification(notificationBean);
                // 流程ID
                auditBean.setNReviewid(reviewId);
                startAuditBean.setNReviewid(reviewId);
                // 操作序列
                int sub = 1;
                auditBean.setSubNo(sub);
                startAuditBean.setSubNo(0);
                // 审核状态值
                auditBean.setCAuditstatus(sub * 10);
                startAuditBean.setCAuditstatus(0);
                // 流程步骤项
                List<ReviewInfoBean> reviewInfo = reviewServiceImpl.getStepByReviewid(reviewId);
                ReviewInfoBean reviewInfoBean;
                if(reviewInfo == null || reviewInfo.size() == 0){
                    log.error("获取流程步骤失败 reviewId {}",reviewId);
                    throw new RuntimeException("获取流程步骤失败");
                }
                reviewInfoBean = reviewInfo.get(0);
                auditBean.setCStep(reviewInfoBean.getNRecordid());
                startAuditBean.setCStep(0);
                // 添加时间
                Date now = new Date();
                auditBean.setDInsertTime(now);
                startAuditBean.setDAuditTime(now);
                startAuditBean.setDInsertTime(now);
                // 查询类别编号
                String distinguishId = auditServiceImpl.getDistinguishById(typeId);
                auditBean.setDistinguishId(distinguishId);
                startAuditBean.setDistinguishId(distinguishId);

                auditServiceImpl.addRecord(startAuditBean);
                int addRecord = auditServiceImpl.addRecord(auditBean);
                if(addRecord > 0){
                    log.debug("-------------------------流程第一步保存成功，生成通知-------------------------");
                    ard.setSuccess(true);
                    // 流程ID
                    notificationBean.setNReviewid(reviewId);
                    // 优先级
                    notificationBean.setCPriority(91);
                    // 步骤
                    notificationBean.setCSubNo(1);
                    // 角色ID
                    ReviewInfoBean stepInfo = reviewServiceImpl.getIdByReviewIdAndSubNo(reviewId, 1);
                    if(stepInfo == null){
                        notificationBean.setNRoleid(0);
                    }else{
                        notificationBean.setNRoleid(stepInfo.getNRoleid());
                    }
                    // URL
                    String Url;
                    if(typeId != 0){
                        Url = notificationServiceImpl.getUrlByTypeId(typeId);
                    }else{
                        Url = "";
                    }
                    notificationBean.setCUrl(Url);
                    // 添加时间
                    notificationBean.setCNotificationTime(now);
     //与邮件有关 占时 关闭
                    int addNotification = notificationServiceImpl.addNotification(notificationBean);
                    if(addNotification > 0){
                        ard.setMessage("All success!");
                    }
                }else {
                    auditServiceImpl.updateVoidAudit(auditBean);
                    ard.setSuccess(false);
                    ard.setMessage("Audit failure!");
                }
            }
        }
        return ard;
    }

    /**
     * 根据编号查询是否有进行审核
     * @param distinguishId
     * @param recordCd  数据cd
     * @param effectiveStartDate
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/checkAudit")
    @ResponseBody
    public AjaxResultDto checkInAudit(String distinguishId, String recordCd, String effectiveStartDate,
                                      HttpServletRequest request, HttpServletResponse response) {
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(recordCd) || StringUtils.isBlank(distinguishId)) {
            _return.setSuccess(true);
            _return.setMessage("params is null");
            return _return;
        }
        // 查询结果
        int count = auditServiceImpl.getInAuditById(distinguishId, recordCd,effectiveStartDate);
        if(count > 0){
            _return.setSuccess(true);
        }else {
            _return.setSuccess(false);
        }
        return _return;
    }
    @RequestMapping("/checkOnlyRole")
    @ResponseBody
    public AjaxResultDto checkOnlyRole(String recordId, int typeId,
                                   HttpServletRequest request,HttpSession session) {
        AjaxResultDto _return = new AjaxResultDto();
        User u = this.getUser(session);
        // 查询记录
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, recordId);
        if(bean == null){
            _return.setSuccess(false);
            _return.setMessage("Null");
            return _return;
        }
        // 判断角色是否符合
        long cStep = bean.getCStep();
        ReviewInfoBean step = reviewServiceImpl.getStepById(cStep);
        Integer roleId;
        if(step == null){
            roleId = 0;
        }else{
            roleId = step.getNRoleid();
        }
        String storeCd = bean.getStoreCd();
        if(StringUtils.isNotBlank(storeCd)){
            int count = auditServiceImpl.checkAuditByUserId(storeCd,u.getUserId(),roleId);
            if(count==0){
                _return.setData(roleId);
                _return.setSuccess(false);
                _return.setMessage("role");
                return _return;
            }
        }
        _return.setSuccess(true);
        return _return;
    }
    /**
     * 判断角色是否符合
     * @param
     * @return
     */
    @RequestMapping("/checkRole")
    @ResponseBody
    public AjaxResultDto checkRole(String recordId, int typeId,
                                   HttpServletRequest request,HttpSession session) {
        AjaxResultDto _return = new AjaxResultDto();
        User u = this.getUser(session);
        // 查询记录
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, recordId);
        if(bean == null){
            _return.setSuccess(false);
            _return.setMessage("Null");
            return _return;
        }
        // 判断记录是否在审核阶段
        int status = bean.getCAuditstatus();
        int temp = status % 10;
        if(temp != 0){
            _return.setSuccess(false);
            _return.setMessage("status");
            return _return;
        }
        // 判断角色是否符合
        long cStep = bean.getCStep();
        ReviewInfoBean step = reviewServiceImpl.getStepById(cStep);
        Integer roleId;
        if(step == null){
            roleId = 0;
        }else{
            roleId = step.getNRoleid();
        }
        String storeCd = bean.getStoreCd();
        if(StringUtils.isNotBlank(storeCd)){
            int count = auditServiceImpl.checkAuditByUserId(storeCd,u.getUserId(),roleId);
            if(count==0){
                _return.setData(roleId);
                _return.setSuccess(false);
                _return.setMessage("role");
                return _return;
            }
        }
        _return.setSuccess(true);
        return _return;
    }

    /**
     * 判断审核是否完成
     * @param
     * @return
     */
    @RequestMapping("/checkFinish")
    @ResponseBody
    public AjaxResultDto checkFinish(String recordId, int typeId,
                                     HttpServletRequest request, HttpServletResponse response) {
        AjaxResultDto _return = new AjaxResultDto();
        _return.setSuccess(false);
        // 查询流程最后一条审核记录
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, recordId);
        if(bean == null){
            _return.setMessage("Null");
            return _return;
        }
        // 判断记录审核状态
        int status = bean.getCAuditstatus();
        if(status==99) {
            //审核完成
            _return.setMessage("Complete");
            _return.setSuccess(true);
        }else {
            _return.setMessage("Unfinished");
        }
        return _return;
    }

    /**
     * 获取当前用户信息和时间
     * @param
     * @return
     */
    @RequestMapping("/sysdate")
    @ResponseBody
    public AjaxResultDto getSysdate(HttpServletRequest request,HttpSession session) {
        AjaxResultDto _return = new AjaxResultDto();
        User u = this.getUser(session);

        _return.setSuccess(true);
        Map<String,Object> map = new HashMap();
        map.put("User",u);
        // 生成当前时间
        map.put("NowTime",new Date());
        _return.setData(map);
        return _return;
    }

    /**
     * 根据记录获取当前审核步骤
     * @param
     * @return
     */
    @RequestMapping("/getStep")
    @ResponseBody
    public AjaxResultDto getStep(String recordId, int typeId,
                                 HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        AjaxResultDto _return = new AjaxResultDto();
        // 判断参数
        if(StringUtils.isBlank(recordId)){
            _return.setSuccess(false);
            _return.setMessage("Param");
            return _return;
        }
        // 查询记录
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, recordId);
        if(bean == null){
            _return.setSuccess(false);
            _return.setMessage("Null");
            return _return;
        }
        // 判断角色是否符合
        long cStep = bean.getCStep();
        ReviewInfoBean step = reviewServiceImpl.getStepById(cStep);

        if (step == null) {
            step = new ReviewInfoBean();
        }
        Integer roleId = step.getNRoleid();
        String storeCd = bean.getStoreCd();
        if(StringUtils.isNotBlank(storeCd)){
            int count = auditServiceImpl.checkAuditByUserId(storeCd,u.getUserId(),roleId);
            if(count==0){
                _return.setSuccess(false);
                _return.setMessage("Inconformity");
                return _return;
            }
        }
        // 返回记录主键ID
        _return.setSuccess(true);
        _return.setData(bean.getNRecordid());
        return _return;
    }

    /**
     * 更新审核信息
     * @param
     * @return
     */
    @RequestMapping("/submit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto submitStep(long auditStepId, String auditUserId, String auditStatus, String auditContent,String detailType,
                                    HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        User user = this.getUser(session);
        AjaxResultDto _return = new AjaxResultDto();

        if(auditStatus == null){
            _return.setSuccess(false);
            _return.setMessage("Param");
            return _return;
        }
        try {
            AuditBean auditStep = auditServiceImpl.getAuditById(auditStepId);
            if(auditStep == null){
                _return.setSuccess(false);
                _return.setMessage("Null");
                return _return;
            }
            // 判断当前步骤项
            long reviewid = auditStep.getNReviewid();
            // 审核通过，判断是否有下一步，生成新的流程
            boolean flag = false;
            // 判断主档类型
            int typeid = auditStep.getNTypeid();
            if(typeid == 0){
                _return.setSuccess(false);
                _return.setMessage("review type error");
                return _return;
            }
            // 记录审核结果
            String _flag = "0";

            if("1".equals(auditStatus)){//审核通过
                long subMax = reviewServiceImpl.getSubMaxByReviewId(reviewid);
                long nowSubNo = auditStep.getSubNo();

                if(subMax == nowSubNo){
                    auditStep.setCAuditstatus(99);
                    auditServiceImpl.updateAuditStatus(auditStep);
                    if(detailType != null){
                        boolean checkFlg = reNewalMsg(detailType,auditStep.getStoreCd(),auditStep.getCRecordCd());
                        if(!checkFlg){
                            _return.setSuccess(false);
                            _return.setMessage("Failed to save inventory information!");
                            return _return;
                        }
                    }
                    _flag = "10";
                }else if(subMax > nowSubNo){
                    flag = true;

                }else{
                    _return.setSuccess(false);
                    _return.setMessage("review step error");
                    return _return;
                }
            }else if("0".equals(auditStatus)){//审核不通过
                auditStep.setCAuditstatus(auditStep.getCAuditstatus()-5);
                auditServiceImpl.updateAuditStatus(auditStep);
                _flag = "5";
            }else{
                _return.setSuccess(false);
                _return.setMessage("Param");
                return _return;
            }
            auditStep.setNOperatorid(user.getUserId());
            auditStep.setDAuditTime(new Date());
            auditStep.setCAuditContent(auditContent);

            int r = auditServiceImpl.updateAuditInfo(auditStep);

            // 审核信息更新完成，取消通知
            // 类型ID
            int typeId = typeid;
            // 主档ID
            String recordCd = auditStep.getCRecordCd();
            // 店铺code
            String storeCd = auditStep.getStoreCd();
            // 适用开始日
            String startDate = auditStep.getEffectiveStartDate();
            // 操作序列
            int subNo = auditStep.getSubNo();
            NotificationBean record = notificationServiceImpl.getByKeyAndSubNo(typeId, subNo, recordCd, startDate);
            if(record != null){
                log.debug("-------------------------已进行审核更新，取消通知显示-------------------------");
                record.setStatus(_flag);
                notificationServiceImpl.updateNotification(record);
            }
            if(r > 0){
                _return.setSuccess(true);
                if(flag){
                    log.debug("-------------------------步骤更新完成，生成流程下一步-------------------------");
                    // 生成新的流程
                    AuditBean newAuditBean = new AuditBean();
                    // 审核流程ID
                    long reviewId = auditStep.getNReviewid();
                    newAuditBean.setNReviewid(reviewId);
                    // 类别ID
                    newAuditBean.setDistinguishId(auditStep.getDistinguishId());
                    // 主档类型ID
                    newAuditBean.setNTypeid(typeId);
                    // 店铺cd
                    newAuditBean.setStoreCd(storeCd);
                    // 主档ID
                    newAuditBean.setCRecordCd(recordCd);
                    // 适用开始日
                    newAuditBean.setEffectiveStartDate(startDate);
                    // 操作序列
                    int newSubNo = subNo + 1;
                    newAuditBean.setSubNo(newSubNo);
                    // 审核状态值
                    newAuditBean.setCAuditstatus(newSubNo * 10);
                    // 添加时间
                    Date now = new Date();
                    newAuditBean.setDInsertTime(now);
                    // 步骤ID
                    ReviewInfoBean stepInfo = reviewServiceImpl.getIdByReviewIdAndSubNo(reviewId, newSubNo);
                    long stepNo;
                    if(stepInfo != null){
                        stepNo = stepInfo.getNRecordid();
                    }else{
                        stepNo = 0;
                    }
                    newAuditBean.setCStep(stepNo);
                    newAuditBean.setNOperatorid("");

                    int add = auditServiceImpl.addRecord(newAuditBean);
                    if(add > 0){
                        log.debug("-------------------------新流程生成成功，生成流程的通知-------------------------");
                        // 新建通知
                        NotificationBean notificationBean = new NotificationBean();
                        // 店铺cd
                        notificationBean.setStoreCd(storeCd);
                        // 流程ID
                        notificationBean.setNReviewid(reviewId);
                        // 优先级
                        notificationBean.setCPriority(91);
                        // 类型ID
                        notificationBean.setNTypeid(typeId);
                        // 主档ID
                        notificationBean.setCRecordCd(recordCd);
                        // 适用开始日
                        notificationBean.setEffectiveStartDate(startDate);
                        // 步骤
                        notificationBean.setCSubNo(newSubNo);
                        // 角色ID
                        if(stepInfo == null){
                            notificationBean.setNRoleid(0);
                        }else{
                            notificationBean.setNRoleid(stepInfo.getNRoleid());
                        }
                        // URL
                        String Url;
                        if(typeId != 0){
                            Url = notificationServiceImpl.getUrlByTypeId(typeId);
                        }else{
                            Url = "";
                        }
                        notificationBean.setCUrl(Url);
                        // 添加时间
                        notificationBean.setCNotificationTime(now);

//                        int addNotification = notificationServiceImpl.addNotification(notificationBean);
//                        if(addNotification > 0){
//                            log.debug("-------------------------审核更新 + 生成新流程、通知 OK-------------------------");
//                        }
                    }
                }else{
                    _return.setMessage("upt");
                    log.debug("------------------------- Completed -------------------------");
                }
            }
            _return.setData(_flag);
            //修改主档审核状态
            int flg = auditServiceImpl.updateRecordStatus(auditStepId,auditStatus,user.getUserId());
            if(flg<1){
                //修改失败事务回滚
                log.error("修改主档审核状态失败 类型:{} 数据id:{}",recordCd,typeId);
                throw new SystemRuntimeException("修改主档审核状态失败 类型:{"+typeId+"} 数据id:{"+recordCd+"}");
            }
        } catch (Exception e){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            _return.setSuccess(false);
            e.printStackTrace();
            log.error("审核失败 {}",e.getMessage());
        }
        return _return;
    }

    /**
     * @Description: 获取审核状态
     * @return _return 返回类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRecordStatus")
    public AjaxResultDto getAuditStatus(HttpSession session, HttpServletRequest request,
                                        String recordId,int typeId,int reviewId) {
        AjaxResultDto _return = new AjaxResultDto();
        if(reviewId!=0){
            typeId = ConstantsAudit.getTypeIdByReviewId(reviewId);
        }
        if(StringUtils.isBlank(recordId)||typeId==0){
            _return.setSuccess(false);
            _return.setMessage("The parameter cannot be null!");
            return _return;
        }
        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            _return.setSuccess(false);
            _return.setMessage("Failed!");
            return _return;
        }
        // 获取审核状态
        Integer status = auditServiceImpl.getAuditStatus(table,key,recordId);
        if(status == null){
            _return.setSuccess(true);
            _return.setMessage("The approval status is empty!");
        }else if(status == 1){//正在审核
            _return.setSuccess(false);
            _return.setMessage("In review, cannot modify!");
        }else if(status == 7){//审核失效
            _return.setSuccess(false);
            _return.setMessage("The approval status is expired and cannot modify!");//审核状态已失效
        }else if(status == 10){//审核成功
            _return.setSuccess(false);
            _return.setMessage("Selected PO is already approved and cannot modify!");//审核状态已完成
        }else if(status == 15){////收货录入完成，未审核
            _return.setSuccess(true);
        }else if(status == 5 || status == 6){//审核驳回、审核撤回
            _return.setSuccess(true);
        }else{
            _return.setSuccess(false);
            _return.setMessage("Approval status unknown!");
        }
        _return.setData(status);
        return _return;
    }

    /**
     * @Description: 修改主档审核状态值
     * @param @return 参数类型
     * @return _return 返回类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/setRecordStatus")
    public AjaxResultDto setRecordStatus(HttpSession session,long auditStepId, String auditStatus) {
        log.info("--------------------执行修改记录审核状态值--------------------");
        AjaxResultDto _return = new AjaxResultDto();

        // 获取最新审核步骤
        AuditBean auditStep = auditServiceImpl.getAuditById(auditStepId);
        if(auditStep == null){
            _return.setSuccess(false);
            _return.setMessage("AuditStep no data");
            return _return;
        }
        long reviewid = auditStep.getNReviewid();
        String recordCd = auditStep.getCRecordCd();
        int typeId = auditStep.getNTypeid();

        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            _return.setSuccess(false);
            _return.setMessage("Failed!");
            return _return;
        }
        int count;
        if(auditStatus.equals("1")){
            // 判断是否已Completed
            long subMax = reviewServiceImpl.getSubMaxByReviewId(reviewid);
            long nowSubNo = auditStep.getSubNo();
            if(subMax == nowSubNo){
                // 记录Completed
                count = auditServiceImpl.updateRecordStatus(table,key, recordCd, 10,reviewid,getUserId(session));
            }else{
                // 记录审核流程未完成
                count = 0;
            }
        }else if(auditStatus.equals("0")){
            // 记录未通过审核
            count = auditServiceImpl.updateRecordStatus(table,key,recordCd, 5,0,null);
        }else{
            _return.setSuccess(false);
            _return.setMessage("Status is unknow");
            return _return;
        }
        if(count == -1){
            _return.setSuccess(false);
            _return.setMessage("Failed!");
            return _return;
        }
        _return.setSuccess(true);
        _return.setMessage("Succeeded!");
        return _return;
    }

    /**
     * 获取初始审核流程图
     */
    @RequestMapping("/getDefaultFlowChart")
    @ResponseBody
    public AjaxResultDto getDefaultFlowChart(long reviewid, HttpServletRequest request, HttpServletResponse response){

        log.info("进入audit/getDefaultFlowChart方法!");
        AjaxResultDto _return = new AjaxResultDto();

        // 查询流程步骤
        List<ReviewInfoBean> reviewStep = reviewServiceImpl.getStepByReviewid(reviewid);
        if(reviewStep == null || reviewStep.size() == 0){
            log.info("-------------------------查询流程步骤为空-------------------------");
            _return.setSuccess(false);
            return _return;
        }

        // 返回结果
        String htmlStrrtn = "";

        // HTML代码的首尾
        String htmlStrHeader = "<html><body><div><table height='50px' width='auto' cellpadding='0' cellspacing='0' style='margin-top:10px;'>";
        String htmlStrFoot = "</table></div></body></html>";

        // 根据审核状态判断选择图片
        String num = "";

        // 拼接<tr>
        StringBuilder tempUrl = new StringBuilder();
        StringBuilder tempText = new StringBuilder();

        // 拼接标示images路径
        String imgUrlBegin = "<td><div align='center'><img src='../images/Button/button_";
        String imgUrlEnd = ".png' width=25px height=25px/></div></td>";

        // 连接线路径
        String imgLine = "<td><div align='center'><img src='../images/line.jpg' width=60px height=25px/></div></td>";

        // 提交审核images
        String start = "<td><div align='center'><img src='../images/Button/start.png' width=25px height=25px/></div></td>";

        // 审核未完成images
        String noStatcOk = "<td><div align='center'><img src='../images/Button/noAccept.png' width=25px height=25px/></div></td>";

        tempUrl.append("<tr><td style=\"min-width:60px\"></td>" + start + imgLine);
        tempText.append("<tr><td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ "Category Staff" +"</div></td>");

        // 每次遍历拼接代码
        String url = "";
        String text = "";
        int i = 1;
        for(ReviewInfoBean step : reviewStep){
            num = i + "";
            url = imgLine + imgUrlBegin + num + "_silver" + imgUrlEnd + imgLine;
            text = "<td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ step.getCRoleName() +"</div></td>";
            tempUrl.append(url);
            tempText.append(text);
            i++;
        }

        tempUrl.append(imgLine + noStatcOk +"<td style=\"min-width:60px\"></td></tr>");

        tempText.append("<td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ "Completed" +"</div></td></tr>");

        htmlStrrtn = htmlStrHeader + tempUrl.toString() + tempText.toString() + htmlStrFoot;

        _return.setData(htmlStrrtn);
        _return.setSuccess(true);
        return _return;
    }

    /**
     * 获取审核流程状态
     */
    @RequestMapping("/getStatus")
    @ResponseBody
    public AjaxResultDto getReviewStatus(String recordId, int typeId,
                                     HttpServletRequest request, HttpServletResponse response){

        log.info("进入audit/getStatus方法!");
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(recordId)|| typeId == 0){
            log.info("-------------------------参数异常-------------------------");
            _return.setSuccess(false);
            return _return;
        }

        // 查询当前审核记录
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, recordId);
        if(bean == null){
            log.info("-------------------------查询审核记录为空-------------------------");
            _return.setSuccess(false);
            return _return;
        }
        int subNo = bean.getSubNo();// 当前步骤项
        int status = bean.getCAuditstatus();// 审核状态值

        // 查询流程、流程步骤
        long reviewid = bean.getNReviewid();
        ReviewBean review = reviewServiceImpl.getRecordById(reviewid);
        String beginStr = "Unknown";
        if(review != null) {
            beginStr = review.getStartRole();
            if(StringUtils.isBlank(beginStr)) {
                beginStr = "Unknown";
            }
        }

        List<ReviewInfoDTO> reviewStep = reviewServiceImpl.getStepBynReviewid(reviewid);
        if(reviewStep == null || reviewStep.size() == 0){
            log.info("-------------------------查询流程步骤为空-------------------------");
            _return.setSuccess(false);
            return _return;
        }

        // 返回结果
        String htmlStrrtn = "";

        // HTML代码的首尾
        String htmlStrHeader = "<html><body><div><table height='50px' width='auto' cellpadding='0' cellspacing='0' style='margin-top:10px;'>";
        String htmlStrFoot = "</table></div></body></html>";

        // 根据审核状态判断选择图片
        String num = "";

        // 拼接<tr>
        StringBuilder tempUrl = new StringBuilder();
        StringBuilder tempText = new StringBuilder();

        // 拼接标示images路径
        String imgUrlBegin = "<td><div align='center'><img src='../images/Button/button_";
        String imgUrlEnd = ".png' width=25px height=25px/></div></td>";

        // 连接线路径
        String imgLine = "<td><div align='center'><img src='../images/line.jpg' width=60px height=25px/></div></td>";

        // 提交审核images
        String start = "<td><div align='center'><img src='../images/Button/start.png' width=25px height=25px/></div></td>";

        // 等待审核images
        //String statcWait = "<td><div align='center'><img src='../images/logwait.gif' width=25px height=25px/></div></td>";
        // Completedimages
        String statcOk = "<td><div align='center'><img src='../images/Button/accept.png' width=25px height=25px/></div></td>";

        // 审核未完成images
        String noStatcOk = "<td><div align='center'><img src='../images/Button/noAccept.png' width=25px height=25px/></div></td>";

        // 审核不通过images
        String statcDeny = "<td><div align='center'><img src='../images/Button/deny.png' width=25px height=25px/></div></td>";

        tempUrl.append("<tr><td style=\"min-width:60px\"></td>" + start + imgLine);
        tempText.append("<tr><td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ beginStr +"</div></td>");

        // 每次遍历拼接代码
        String url = "";
        String text = "";
        boolean flag = false;
        int i = 1;
        for(ReviewInfoDTO step : reviewStep){
            num = i + "";
            if(subNo > i){
                url = imgLine + imgUrlBegin + num + "_blue" + imgUrlEnd + imgLine;
            }else if(subNo < i){
                url = imgLine + imgUrlBegin + num + "_silver" + imgUrlEnd + imgLine;
            }else{
                if(status % 10 == 5){
                    url = imgLine + statcDeny + imgLine;
                }else if(status == 99){
                    flag = true;
                    url = imgLine + imgUrlBegin + num + "_blue" + imgUrlEnd + imgLine;
                }else{
                    // 不使用动态图片
                    //url = imgLine + statcWait + imgLine;
                    url = imgLine + imgUrlBegin + num + "_silver" + imgUrlEnd + imgLine;
                }
            }
            text = "<td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ step.getCRoleName() +"</div></td>";
            tempUrl.append(url);
            tempText.append(text);
            i++;
        }

        if(flag){
            tempUrl.append(imgLine + statcOk +"<td style=\"min-width:60px\"></td></tr>");
        }else{
            tempUrl.append(imgLine + noStatcOk +"<td style=\"min-width:60px\"></td></tr>");
        }
        tempText.append("<td colspan=\"3\"><div align='center' style= 'font-size: 12px;'>"+ "Completed" +"</div></td></tr>");

        htmlStrrtn = htmlStrHeader + tempUrl.toString() + tempText.toString() + htmlStrFoot;

        _return.setData(htmlStrrtn);
        _return.setSuccess(true);
        return _return;
    }

    /**
     * 修正实时库存和bi库存
     * @param detailType
     * @param storeCd
     * @param recordCd
     * @return
     */
    private boolean reNewalMsg(String detailType,String storeCd,String recordCd){
        boolean checkFlg = true;
        List<OD0010TDTO> _reDetailsList = null;
        List<Sk0020DTO> _sk0020List = null;
        switch (detailType) {
            case "tmp_receive":
                _reDetailsList = allRtService.getReceiveItemList(recordCd);
                checkFlg = checkReceiveNewMsg(detailType,storeCd,_reDetailsList);
                return checkFlg;
            case "tmp_return":
                _reDetailsList = allRtService.getReturnItemList(recordCd);
                checkFlg = checkReceiveNewMsg(detailType,storeCd,_reDetailsList);
                return checkFlg;
            case "tmp_receive_corr":
                _reDetailsList = allRtService.getReCorrItemList(recordCd);
                if(_reDetailsList.size()>0){
                    checkFlg = checkReceiveNewMsg(detailType,storeCd,_reDetailsList);
                }else {
                    checkFlg = true;
                }
                return checkFlg;
            case "tmp_return_corr":
                _reDetailsList = allRtService.getReCorrItemList(recordCd);
                if(_reDetailsList.size()>0){
                    checkFlg = checkReceiveNewMsg(detailType,storeCd,_reDetailsList);
                }else {
                    checkFlg = true;
                }
                return checkFlg;
            case "tmp_on_order":
                // 紧急订货审核通过，增加在途量
                _reDetailsList = allRtService.getReturnItemList(recordCd);
                checkFlg = checkReceiveNewMsg(detailType,storeCd,_reDetailsList);
                return checkFlg;
            case "tmp_adjustment":
                _sk0020List = allRtService.getVoucherItemList(recordCd);
                checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                return checkFlg;
            case "tmp_write_off":
                _sk0020List = allRtService.getVoucherItemList(recordCd);
                checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                return checkFlg;
            case "tmp_transfer_out":
                _sk0020List = allRtService.getVoucherItemList(recordCd);
                checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                return checkFlg;
            case "tmp_transfer_in":
                _sk0020List = allRtService.getVoucherItemList(recordCd);
                checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                return checkFlg;
            case "tmp_transfer_out_corr":
                Sk0010DTO sk0010OutDTO = allRtService.getVoucherHead(recordCd,storeCd);
                recordCd = sk0010OutDTO.getVoucherNo1();
                // 得到_sk0020List
                _sk0020List = allRtService.getTranferCorrList(recordCd,storeCd,sk0010OutDTO.getVoucherType());
                if(_sk0020List.size()>0){
                    checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                }else {
                    checkFlg = true;
                }
                return checkFlg;
            case "tmp_transfer_in_corr":
                Sk0010DTO sk0010InDTO = allRtService.getVoucherHead(recordCd,storeCd);
                recordCd = sk0010InDTO.getVoucherNo1();
                // 得到_sk0020List
                _sk0020List = allRtService.getTranferCorrList(recordCd,storeCd,sk0010InDTO.getVoucherType());
                if(_sk0020List.size()>0){
                    checkFlg = checkReNewMsg(detailType,storeCd,_sk0020List);
                }else {
                    checkFlg = true;
                }
                return checkFlg;
            case "tmp_stock_take": // 盘点 走库存调整
                detailType = "tmp_adjustment";
                List<StocktakeProcessItemsDTO> stockItemList = allRtService.getStockItemList(recordCd,storeCd);
                checkFlg = checkRtStockMsg(detailType,storeCd,stockItemList);
                return checkFlg;
            case "tmp_ci_adjustment":
                List<StocktakeItemDTOC> pi0110List =  custEntryMapper.getPI0130ByPrimary(recordCd,storeCd);
                checkFlg = checkCostItemStock(detailType,storeCd,pi0110List);
                return checkFlg;
        }

    return false;
    }

    // inventory下修正库存共通
    protected boolean checkReNewMsg(String detailType,String storeCd,List<Sk0020DTO> _sk0020List){
        boolean checkFlg = true;
        // 判断母货号是否允许操作（Group Sale子母货号是否分开管理  0：分开 1：不分开）
        // String _val = cm9060Service.getValByKey("0634");
        // boolean parentFlg = "0".equals(_val) ? true : false;
        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        if(_sk0020List.size() > 0){
            for(Sk0020DTO sk020:_sk0020List){
                SaveInventoryQty saveDetail = new SaveInventoryQty();
                saveDetail.setDetailType(detailType);
                saveDetail.setStoreCd(storeCd);
                saveDetail.setArticleId(sk020.getArticleId());
                if(sk020.getQty1() == null){
                    sk020.setQty1(BigDecimal.ZERO);
                }
                if(sk020.getQty2() == null){
                    sk020.setQty2(BigDecimal.ZERO);
                }
                 /*stripTrailingZeros() 去除小数点
                 toPlainString() 转换字符串*/
                float qty1 = Float.parseFloat(sk020.getQty1().stripTrailingZeros().toPlainString());
                float qty2 = Float.parseFloat(sk020.getQty2().stripTrailingZeros().toPlainString());

                if(detailType.equals("tmp_transfer_out") ){
                    qty1 = qty2;
                }
                saveDetail.setInventoryQty(qty1);
                if(saveDetail.getInventoryQty() == 0.0){
                    continue;
                }

                saveRtQtyList.add(saveDetail);
                parentList.add(sk020.getArticleId());
            }

            if(saveRtQtyList.size() == 0){
               return true;
            }
            // 修正商品实时库存（修正bi库存时,有可能会修改saveRtQtyList的数量的正负号，因此先进行修正实时库存）
            RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
            if(!rtInvContent.getStatus().equals("0")){
                checkFlg = false;
            }
            // 修正bi库存
            String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
            if(rtBi == null){
                checkFlg = false;
            }

            // 判断是否存在group商品母货号
            List<SaveInventoryQty> groupSavertList = new ArrayList<>();
            List<String> result = ma1200Service.checkList(parentList);
            log.info("<<<result:"+result.size());
            if(result.size()>0){
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getChildDetail(result);
                if(_result.size()>0){
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty:saveRtQtyList) {
                            if(saveQty.getArticleId().equals(map.get("parentArticleId"))){
                                float qty1 = saveQty.getInventoryQty();
                                // 对子商品进行库存操作
                                float articleQty = qty1*Integer.parseInt(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                saveGroupDetail.setArticleId(childeArticleId);
                                saveGroupDetail.setInventoryQty(articleQty);
                                saveGroupDetail.setDetailType(detailType);
                                saveGroupDetail.setStoreCd(storeCd);
                                groupSavertList.add(saveGroupDetail);

                            }
                        }
                    }
                }
                // 修正子商品实时库存
                RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                if (!rtChildContent.getStatus().equals("0")) {
                    checkFlg = false;
                }
                // 修正bi库存
                String groupRtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                if(groupRtBi == null){
                    checkFlg = false;
                }
            }

            // 判断是否存在BOM商品母货号
            List<SaveInventoryQty> bomSavertList = new ArrayList<>();
            List<String> bomResult = ma1200Service.checkBOMList(parentList);
            if(bomResult.size()>0){
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getBOMChildDetail(bomResult);
                if (_result.size() > 0) {
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty : saveRtQtyList) {
                            if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                float qty1 = saveQty.getInventoryQty();
                                // 对子商品进行库存操作
                                float articleQty = qty1*Float.parseFloat(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                saveBomDetail.setArticleId(childeArticleId);
                                saveBomDetail.setInventoryQty(articleQty);
                                saveBomDetail.setDetailType(detailType);
                                saveBomDetail.setStoreCd(storeCd);
                                bomSavertList.add(saveBomDetail);
                            }
                        }
                    }
                    // 修正子商品实时库存
                    RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                    if (!rtChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                    // 修正bi库存
                    String bomRtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                    if(bomRtBi == null){
                        checkFlg = false;
                    }
                }
            }
            return checkFlg;
        }
        return checkFlg;
    }

    /**
     * 收退货修正库存
     * @param detailType
     * @param storeCd
     * @param _reDetailsList
     * @return
     */
    protected boolean checkReceiveNewMsg(String detailType,String storeCd,List<OD0010TDTO> _reDetailsList){
        boolean checkFlg = true;
        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();

        if(_reDetailsList.size()>0){
            for(OD0010TDTO odDto : _reDetailsList) {
                SaveInventoryQty saveDetail = new SaveInventoryQty();
                saveDetail.setDetailType(detailType);
                saveDetail.setStoreCd(storeCd);
                saveDetail.setArticleId(odDto.getArticleId());
                if(odDto.getReceiveQty() == null){
                    odDto.setReceiveQty(BigDecimal.ZERO);
                }
                if(odDto.getReturnQty() == null){
                    odDto.setReturnQty(BigDecimal.ZERO);
                }

                float receiveQty = 0; float returnQty = 0;float orderQty = 0;
                if(detailType.equals("tmp_receive")){
                    receiveQty = Float.parseFloat(odDto.getReceiveQty().stripTrailingZeros().toPlainString());
                    saveDetail.setInventoryQty(receiveQty);
                }else if(detailType.equals("tmp_return")){
                    returnQty = Float.parseFloat(odDto.getReceiveQty().stripTrailingZeros().toPlainString());
                    saveDetail.setInventoryQty(returnQty);
                }else if(detailType.equals("tmp_on_order")){
                     // 紧急订货审核，增加在途量
                    orderQty = Float.parseFloat(odDto.getReturnQty().stripTrailingZeros().toPlainString());
                    saveDetail.setInventoryQty(orderQty);
                }else if(detailType.equals("tmp_receive_corr")){
                    float receiveCorrQty = Float.parseFloat(odDto.getReceiveQty().stripTrailingZeros().toPlainString());
                    saveDetail.setInventoryQty(receiveCorrQty);
                }else if(detailType.equals("tmp_return_corr")){
                    float returnCorrQty = Float.parseFloat(odDto.getReceiveQty().stripTrailingZeros().toPlainString());
                    saveDetail.setInventoryQty(returnCorrQty);
                }
                if(saveDetail.getInventoryQty() == 0.0){
                    continue;
                }
                // 传一个List至ES,取得实时库存信息
                saveRtQtyList.add(saveDetail);
                parentList.add(odDto.getArticleId());
            }

            if(saveRtQtyList.size() == 0){
                return true;
            }

            // 收货增加收货量，减去在途量
            if (detailType.equals("tmp_receive")) {

                // 修正商品实时库存
                RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtInvContent.getStatus().equals("0")){
                    checkFlg = false;
                }
                // 修正bi库存
                String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
                if(rtBi == null){
                    checkFlg = false;
                }

                for(int i=0;i<saveRtQtyList.size();i++){
                    saveRtQtyList.get(i).setDetailType("tmp_on_order");
                    saveRtQtyList.get(i).setReceiveQty(0.0f);
                    saveRtQtyList.get(i).setInventoryQty(-saveRtQtyList.get(i).getInventoryQty());
                }
                // 修正商品实时库存
                RtInvContent rtOnOrderContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtOnOrderContent.getStatus().equals("0")){
                    checkFlg = false;
                }

            }else if(detailType.equals("tmp_on_order")){
                // 修正商品实时库存
                RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtInvContent.getStatus().equals("0")){
                    checkFlg = false;
                }
            }else {
                // 修正商品实时库存
                RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtInvContent.getStatus().equals("0")){
                    checkFlg = false;
                }
                // 修正bi库存
                String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
                if(rtBi == null){
                    checkFlg = false;
                }

            }

            // 判断是否存在group商品母货号
            List<SaveInventoryQty> groupSavertList = new ArrayList<>();
            List<String> result = ma1200Service.checkList(parentList);
            log.info("<<<result:" + result.size());
            if (result.size() > 0) {
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getChildDetail(result);
                if (_result.size()>0) {
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty:saveRtQtyList) {
                            if(saveQty.getArticleId().equals(map.get("parentArticleId"))){
                                float reQty = Math.abs(saveQty.getInventoryQty());
                                // 对子商品进行库存操作
                                float articleReQty = reQty * Integer.parseInt(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                saveGroupDetail.setArticleId(childeArticleId);
                                saveGroupDetail.setInventoryQty(articleReQty);
                                saveGroupDetail.setDetailType(detailType);
                                saveGroupDetail.setStoreCd(storeCd);
                                groupSavertList.add(saveGroupDetail);
                            }
                        }
                    }
                    if(detailType.equals("tmp_on_order")){
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                    }else {
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                        // 修正bi库存
                        String rtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                        if(rtBi == null){
                            checkFlg = false;
                        }
                    }
                }
            }
            // 判断是否存在BOM商品母货号
            List<SaveInventoryQty> bomSavertList = new ArrayList<>();
            List<String> bomResult = ma1200Service.checkBOMList(parentList);
            log.info("<<<result:" + result.size());
            if (bomResult.size() > 0) {
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getBOMChildDetail(bomResult);
                if (_result.size() > 0) {
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty : saveRtQtyList) {
                            if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                float reQty = Math.abs(saveQty.getInventoryQty());
                                // 对子商品进行库存操作
                                float articleReQty = reQty * Float.parseFloat(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                saveBomDetail.setArticleId(childeArticleId);
                                saveBomDetail.setInventoryQty(articleReQty);
                                saveBomDetail.setDetailType(detailType);
                                saveBomDetail.setStoreCd(storeCd);
                                bomSavertList.add(saveBomDetail);
                            }
                        }
                    }
                    if(detailType.equals("tmp_on_order")){
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                    }else {
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                        // 修正bi库存
                        String rtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                        if(rtBi == null){
                            checkFlg = false;
                        }
                    }
                }
            }
            return checkFlg;
        }
        return checkFlg;
    }

    // 盘点库存调整
    protected boolean checkRtStockMsg(String detailType,String storeCd,List<StocktakeProcessItemsDTO> stockItemList){
        boolean checkFlg = true;

        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        if(stockItemList.size() > 0){
            for(int i=0;i<stockItemList.size();i++){
                StocktakeProcessItemsDTO stockTake = stockItemList.get(i);
                SaveInventoryQty saveDetail = new SaveInventoryQty();
                saveDetail.setDetailType(detailType);
                saveDetail.setStoreCd(storeCd);
                saveDetail.setArticleId(stockTake.getArticleId());
                if(stockTake.getVariance() == null){
                    stockTake.setVariance("0");
                }

                float varianceQty = Float.parseFloat(stockTake.getVariance());
                if(varianceQty == 0.0){
                    continue;
                }
                saveDetail.setInventoryQty(varianceQty);
                saveRtQtyList.add(saveDetail);
                parentList.add(stockTake.getArticleId());
            }
            // 修正商品实时库存
            RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
            if(!rtInvContent.getStatus().equals("0")){
                checkFlg = false;
            }

            // 修正bi库存
            String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
            if(rtBi == null){
                checkFlg = false;
            }

            // 判断是否存在group商品母货号
            List<SaveInventoryQty> groupSavertList = new ArrayList<>();
            List<String> result = ma1200Service.checkList(parentList);
            log.info("<<<result:"+result.size());
            if(result.size()>0){
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getChildDetail(result);
                if(_result.size()>0){
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty:saveRtQtyList) {
                            if(saveQty.getArticleId().equals(map.get("parentArticleId"))){
                                float qty1 = saveQty.getInventoryQty();
                                // 对子商品进行库存操作
                                float articleQty = qty1*Integer.parseInt(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                saveGroupDetail.setArticleId(childeArticleId);
                                saveGroupDetail.setInventoryQty(articleQty);
                                saveGroupDetail.setDetailType(detailType);
                                saveGroupDetail.setStoreCd(storeCd);
                                groupSavertList.add(saveGroupDetail);
                            }
                        }
                    }
                }
                // 修正子商品实时库存
                RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                if (!rtChildContent.getStatus().equals("0")) {
                    checkFlg = false;
                }
                // 修正bi库存
                String groupRtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                if(groupRtBi == null){
                    checkFlg = false;
                }
            }

            // 判断是否存在BOM商品母货号
            List<SaveInventoryQty> bomSavertList = new ArrayList<>();
            List<String> bomResult = ma1200Service.checkBOMList(parentList);
            if(bomResult.size()>0){
                // 取得子商品明细
                List<Map<String, String>> _result = ma1200Service.getBOMChildDetail(bomResult);
                if (_result.size() > 0) {
                    for (Map<String, String> map : _result) {
                        for (SaveInventoryQty saveQty : saveRtQtyList) {
                            if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                float qty1 = saveQty.getInventoryQty();
                                // 对子商品进行库存操作
                                float articleQty = qty1*Float.parseFloat(map.get("childQty"));
                                String childeArticleId = map.get("childArticleId");
                                SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                saveBomDetail.setArticleId(childeArticleId);
                                saveBomDetail.setInventoryQty(articleQty);
                                saveBomDetail.setDetailType(detailType);
                                saveBomDetail.setStoreCd(storeCd);
                                bomSavertList.add(saveBomDetail);
                            }
                        }
                    }
                    // 修正子商品实时库存
                    RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                    if (!rtChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                    // 修正bi库存
                    String groupRtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                    if(groupRtBi == null){
                        checkFlg = false;
                    }
                }
            }
            return checkFlg;
        }
        return checkFlg;
    }

    /**
     * Cost Item Adjustment Management 费用商品
     * @param detailType
     * @param storeCd
     * @param stockItemList
     * @return
     */
    protected boolean checkCostItemStock(String detailType,String storeCd,List<StocktakeItemDTOC> stockItemList){
        boolean checkFlg = true;

        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        if(stockItemList.size()>0){
            for(int i=0;i<stockItemList.size();i++){
                StocktakeItemDTOC stockTake = stockItemList.get(i);
                SaveInventoryQty saveDetail = new SaveInventoryQty();
                saveDetail.setDetailType(detailType);
                saveDetail.setStoreCd(storeCd);
                saveDetail.setArticleId(stockTake.getArticleId());

                saveDetail.setInventoryQty(Float.parseFloat(stockTake.getQty().toString()));
                saveRtQtyList.add(saveDetail);
                parentList.add(stockTake.getArticleId());
            }

            // List转jackJosn字符串
            String articleIdListJson = null;
            boolean checkData = true;
            try {
                if(parentList.size() == 0){
                    return false;
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                articleIdListJson = new ObjectMapper().writeValueAsString(parentList);

                String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+storeCd
                        +"/*/*/*/*/*/" + inEsTime+"/*/*";
                String urlData = RealTimeInventoryQueryServiceImpl.RequestPost(articleIdListJson,connUrl);
                if(urlData == null || "".equals(urlData)){
                    String message = "Failed to connect to live inventory data！";
                    checkData = false;
                }

                if(checkData){
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
                        for(SaveInventoryQty saveQty : saveRtQtyList){
                            for (RealTimeDto realTimeDto : realTimeDto2) {
                                if (realTimeDto.getArticle_id().equals(saveQty.getArticleId())) {
                                  // 计算实时库存数量
                                    BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                            .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                            .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                            .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                                    saveQty.setRealtimeQty(Float.parseFloat(rTimeQty.toString()));
                                    saveQty.setInventoryQty(saveQty.getInventoryQty()-saveQty.getRealtimeQty());
                                }
                            }
                        }
                    }
                }

                // 修正商品实时库存
                RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtInvContent.getStatus().equals("0")){
                    checkFlg = false;
                }

                // 修正bi库存
                String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
                if(rtBi == null){
                    checkFlg = false;
                }

                // 判断是否存在group商品母货号
                List<SaveInventoryQty> groupSavertList = new ArrayList<>();
                List<String> result = ma1200Service.checkList(parentList);
                log.info("<<<result:"+result.size());
                if(result.size()>0){
                    // 取得子商品明细
                    List<Map<String, String>> _result = ma1200Service.getChildDetail(result);
                    if(_result.size()>0){
                        for (Map<String, String> map : _result) {
                            for (SaveInventoryQty saveQty:saveRtQtyList) {
                                if(saveQty.getArticleId().equals(map.get("parentArticleId"))){
                                    float qty1 = saveQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1*Integer.parseInt(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                    saveGroupDetail.setArticleId(childeArticleId);
                                    saveGroupDetail.setInventoryQty(articleQty);
                                    saveGroupDetail.setDetailType(detailType);
                                    saveGroupDetail.setStoreCd(storeCd);
                                    groupSavertList.add(saveGroupDetail);
                                }
                            }
                        }
                    }
                    // 修正子商品实时库存
                    RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                    if (!rtChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                    // 修正bi库存
                    String groupRtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                    if(groupRtBi == null){
                        checkFlg = false;
                    }
                }

                // 判断是否存在BOM商品母货号
                List<SaveInventoryQty> bomSavertList = new ArrayList<>();
                List<String> bomResult = ma1200Service.checkBOMList(parentList);
                if(bomResult.size()>0){
                    // 取得子商品明细
                    List<Map<String, String>> _result = ma1200Service.getBOMChildDetail(bomResult);
                    if (_result.size() > 0) {
                        for (Map<String, String> map : _result) {
                            for (SaveInventoryQty saveQty : saveRtQtyList) {
                                if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                    float qty1 = saveQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1*Float.parseFloat(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                    saveBomDetail.setArticleId(childeArticleId);
                                    saveBomDetail.setInventoryQty(articleQty);
                                    saveBomDetail.setDetailType(detailType);
                                    saveBomDetail.setStoreCd(storeCd);
                                    bomSavertList.add(saveBomDetail);
                                }
                            }
                        }
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                        // 修正bi库存
                        String groupRtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                        if(groupRtBi == null){
                            checkFlg = false;
                        }
                    }
                }
                return checkFlg;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return checkFlg;
    }
}
