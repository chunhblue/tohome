package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.audit.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVHeadResult;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.AuditTaskService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.TaskService;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/auditMessage")
public class AuditTaskController extends BaseAction{
    @Autowired
    AuditTaskService auditTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    protected IReviewService reviewServiceImpl;
    @Autowired
    protected INotificationService notificationServiceImpl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private ReturnsDailyService returnsDailyService;

    /**
     * 审核任务画面一览
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/pendingApprovals",method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_INF_AUDIT_LIST_VIEW})
    public ModelAndView pendingApprovals(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入审核任务画面", u.getUserId());
        ModelAndView mv = new ModelAndView("auditMessage/auditPending");
        mv.addObject("useMsg", "审核任务画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 我发起审核被驳回任务画面画面一览
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/rejectedApprovals",method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_INF_REJECTED_LIST_VIEW})
    public ModelAndView rejectedApprovals(HttpServletRequest request, HttpSession session,
                                      Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入我发起审核被驳回任务画面", u.getUserId());
        ModelAndView mv = new ModelAndView("auditMessage/auditRejected");
        mv.addObject("useMsg", "我发起审核被驳回任务画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 我发起的审核任务画面一览
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/mySubmissions",method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_INF_DOCUMENT_LIST_VIEW})
    public ModelAndView mySubmissions(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入我发起的审核任务画面", u.getUserId());
        ModelAndView mv = new ModelAndView("auditMessage/mySubmissions");
        mv.addObject("useMsg", "我发起的审核任务画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 我的审核任务画面一览
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/myMessage",method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_INF_MESSAGE_LIST_VIEW})
    public ModelAndView myMessage(HttpServletRequest request, HttpSession session,
                                      Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入我的信息画面", u.getUserId());
        ModelAndView mv = new ModelAndView("auditMessage/myMessage");
        mv.addObject("useMsg", "我的信息画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取审核类型
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApprovalType")
    public List<AutoCompleteDTO> getApprovalType(HttpSession session) {
        return taskService.getApprovalType();
    }

    /**
     * 获取审核待办
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPendingApprovals", method = RequestMethod.GET)
    @ResponseBody
    public GridDataDTO<ReviewTaskGridDTO> getPendingApprovals(HttpServletRequest request, HttpSession session,
                                                              ReviewTaskParamDTO param) {
        User u = this.getUser(session);
        param.setOperatorId(u.getUserId());
        return taskService.getPendingApprovals(param);
    }

    /**
     * 获取我的提交审核
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSubmissionsApprovals", method = RequestMethod.GET)
    @ResponseBody
    public GridDataDTO<ReviewTaskGridDTO> getSubmissionsApprovals(HttpServletRequest request, HttpSession session,
                                                                  ReviewTaskParamDTO param) {
        User u = this.getUser(session);
        param.setOperatorId(u.getUserId());
        return taskService.getSubmissionsApprovals(param);
    }

    /**
     * 获取我的提交被驳回的审核
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRejectedApprovals", method = RequestMethod.GET)
    @ResponseBody
    public GridDataDTO<ReviewTaskGridDTO> getRejectedApprovals(HttpServletRequest request, HttpSession session,
                                                                  ReviewTaskParamDTO param) {
        User u = this.getUser(session);
        param.setOperatorId(u.getUserId());
        return taskService.getRejectedApprovals(param);
    }

    /**
     * 获取我的提交审核
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMyMessage", method = RequestMethod.GET)
    @ResponseBody
    public GridDataDTO<ReviewTaskGridDTO> getMyMessage(HttpServletRequest request, HttpSession session,
                                                                  ReviewTaskParamDTO param) {
        User u = this.getUser(session);
        param.setOperatorId(u.getUserId());
        return taskService.getMyMessage(param);
    }

    /**
     * 批量审核/撤销
     * @param auditData 审核数据
     * @param auditStatus 审核状态  0 不通过 1 通过 2 撤销
     * @param auditContent 审核内容
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batchAudit")
    @Permission(codes = {
            PermissionCode.CODE_SC_INF_DOCUMENT_WITHDRAW,
            PermissionCode.CODE_SC_INF_AUDIT_AUDIT
    })
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto batchWithdraw(HttpServletRequest request, HttpSession session,
                                  String auditData,String auditStatus,String auditContent) {
        User u = this.getUser(session);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        if(StringUtils.isBlank(auditData)){
            ard.setMessage("The withdraw parameter cannot be null");
        }
        try {
            List<Map<String,Object>> auditRecords = new Gson().fromJson(auditData, new TypeToken<List<Map<String,Object>>>(){}.getType());
            for (Map<String,Object> map : auditRecords) {
                String recordCd = String.valueOf(map.get("recordCd"));
                Integer typeId = Integer.parseInt(String.valueOf(map.get("typeId")));

                // 查询当前审核记录
                AuditBean auditStep = auditServiceImpl.getIdByRecordId(typeId, recordCd);
                if (auditStep==null) {
                    continue ;
                }
                int c_auditStatus = auditStep.getCAuditstatus();
                // 驳回状态或者审核通过或者撤销状态 失效
                if (c_auditStatus%10 == 5 || c_auditStatus == 99 || c_auditStatus%10 == 6 || c_auditStatus%10 == 7) {
                    continue;
                }

                Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
                String table = String.valueOf(auditInfo.get("table"));
                String key = String.valueOf(auditInfo.get("key"));
                if("null".equals(table)||"null".equals(key)){
                    throw new RuntimeException("获取审核类型对应表和字段为空!");
                }
                // 判断当前步骤项
                long reviewid = auditStep.getNReviewid();
                // 审核通过，判断是否有下一步，生成新的流程
                boolean flag = false;
                // 记录审核结果
                String _flag = "0";
                int updateFlg = 0;
                if("1".equals(auditStatus)){//审核通过
                    long subMax = reviewServiceImpl.getSubMaxByReviewId(reviewid);
                    long nowSubNo = auditStep.getSubNo();
                    if(subMax == nowSubNo){
                        auditStep.setCAuditstatus(99);
                        auditServiceImpl.updateAuditStatus(auditStep);
                        //修改表数据审核状态
                        updateFlg = auditServiceImpl.updateRecordStatus(table,key, auditStep.getCRecordCd(), 10,reviewid,u.getUserId());
                        _flag = "10";
                    }else if(subMax > nowSubNo){
                        flag = true;
                    }else{
                        log.error("流程步骤出错 Record Cd: {}", auditStep.getCRecordCd());
                        RuntimeException e = new RuntimeException("review step error");
                        throw e;
                    }
                }else if("0".equals(auditStatus)){//审核不通过
                    auditStep.setCAuditstatus(auditStep.getCAuditstatus()-5);
                    auditServiceImpl.updateAuditStatus(auditStep);
                    //修改表数据审核状态
                    updateFlg = auditServiceImpl.updateRecordStatus(table,key, auditStep.getCRecordCd(), 5,0,null);
                    _flag = "5";
                }else if("2".equals(auditStatus)){//审核撤销
                    auditStep.setCAuditstatus(auditStep.getCAuditstatus()-4);
                    auditServiceImpl.updateAuditStatus(auditStep);
                    //修改表数据审核状态
                    updateFlg = auditServiceImpl.updateRecordStatus(table,key, auditStep.getCRecordCd(), 6,0,null);
                    _flag = "6";
                }else{
                    continue ;
                }
                if(updateFlg <0){//修改表状态失败
                    throw new RuntimeException("修改表状态失败!");
                }
                auditStep.setNOperatorid(u.getUserId());
                auditStep.setDAuditTime(new Date());
                auditStep.setCAuditContent(auditContent);
                //更新审核时间、审核内容、审核人
                int r = auditServiceImpl.updateAuditInfo(auditStep);

                // 操作序列
                int subNo = auditStep.getSubNo();
                NotificationBean record = notificationServiceImpl.getByKeyAndSubNo(typeId, subNo, auditStep.getCRecordCd(), "00000000");
                if(record != null){
                    log.debug("-------------------------已进行审核更新，取消通知显示-------------------------");
                    record.setStatus(_flag);
                    notificationServiceImpl.updateNotification(record);
                }
                if(r > 0){
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

                        // 主档ID
                        newAuditBean.setCRecordCd(auditStep.getCRecordCd());
                        // 适用开始日
                        newAuditBean.setEffectiveStartDate("00000000");
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
                            // 流程ID
                            notificationBean.setNReviewid(reviewId);
                            // 优先级
                            notificationBean.setCPriority(91);
                            // 类型ID
                            notificationBean.setNTypeid(typeId);
                            // 主档ID
                            notificationBean.setCRecordCd(auditStep.getCRecordCd());

                            // 适用开始日
                            notificationBean.setEffectiveStartDate("00000000");
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

                            int addNotification = notificationServiceImpl.addNotification(notificationBean);
                            if(addNotification > 0){
                                log.debug("-------------------------审核更新 + 生成新流程、通知 OK-------------------------");
                            }
                        }
                    }else{
                        log.debug("------------------------- Completed -------------------------");
                    }
                }
            }
        }catch (Exception e){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ard.setSuccess(false);
            e.printStackTrace();
            log.error("批量审核失败 {}",e.getMessage());
        }
        return ard;
    }

    /**
     * 审核请求跳转
     * @param request
     * @param session
     * @return
     */
//    @Permission(codes = {
//            PermissionCode.CODE_SC_DETAILS_VIEW,//供应商订货查看详细
//            PermissionCode.CODE_SC_ST_ADJUST_VIEW,//库存调整
//            PermissionCode.CODE_SC_ST_SCRAP_VIEW,//库存报废
//            //库存调拨
//            PermissionCode.CODE_SC_PD_PROCESS_VIEW,//盘点
//            PermissionCode.CODE_SC_W_RETURN_VIEW,//向dc退货查看详细
//            PermissionCode.CODE_SC_V_RETURN_VIEW//向供应商退货查看详细
//    })
    @RequestMapping(value = "/auditSkip",method = RequestMethod.GET)
    public ModelAndView auditSkip(HttpServletRequest request, HttpSession session,
                                  String url,int typeId,String recordCd) {
        User u = this.getUser(session);
        ModelAndView mv = new ModelAndView(url);
        if(StringUtils.isBlank(url)||typeId<=0||StringUtils.isBlank(recordCd)){
            //参数为空跳转出错页面
            mv.setViewName("/error");
        }
        switch (typeId) {
            case ConstantsAudit.TYPE_ORDER://订货
                log.debug("User:{} 进入订货一览", u.getUserId());
                mv.addObject("use",0);//查看
                mv.addObject("orderId", recordCd);
                //获取业务时间
                String date = cm9060Service.getValByKey("0000");
                mv.addObject("date", date);
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_DC);
                mv.addObject("useMsg", "订货一览");
                break;
            case ConstantsAudit.TYPE_ORDER_DC://dc紧急订货
                log.debug("User:{} 进入dc紧急订货详细", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("orderId", recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_DC);
                mv.addObject("useMsg", "dc紧急订货详细");
                break;
            case ConstantsAudit.TYPE_ORDER_STOCK_SCRAP://库存报废
                log.debug("User:{} 库存报废管理查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                Sk0010DTO dto = taskService.getStockScrap(recordCd);
                //参数为空跳转出错页面
                if(dto==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", dto);
                mv.addObject("viewSts", "view");
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_SCRAP);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_STOCK_SCRAP);
                mv.addObject("useMsg", "库存报废管理查看画面");
                break;
            case ConstantsAudit.TYPE_ORDER_TRANSFEROUT://库存调拨 OUT
                log.debug("User:{} 进入 店间调拨转出管理详情查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                Sk0010DTO dto1 = taskService.getStockScrap(recordCd);
                //参数为空跳转出错页面
                if(dto1==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", dto1);
                mv.addObject("viewSts", "view");
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFEROUT);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFEROUT);
                mv.addObject("useMsg", "店间调出管理详情查看画面");
                break;
            case ConstantsAudit.TYPE_ORDER_TRANSFERIN://库存调拨 IN
                log.debug("User:{} 进入 店间调拨转入管理详情查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                Sk0010DTO dto3 = taskService.getStockScrap(recordCd);
                //参数为空跳转出错页面
                if(dto3==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", dto3);
                mv.addObject("viewSts", "view");
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFERIN);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFERIN);
                mv.addObject("useMsg", "店间调入管理详情查看画面");
                break;
            case ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT://库存调整
                log.debug("User:{} 库存调整详细查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                Sk0010DTO dto2 = taskService.getStockScrap(recordCd);
                //参数为空跳转出错页面
                if(dto2==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", dto2);
                mv.addObject("viewSts", "view");
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_STOCK_ADJUSTMENT);
                mv.addObject("useMsg", "库存调整详细查看画面");
                break;
            case ConstantsAudit.TYPE_ORDER_TAKE_STOCK://盘点
                log.debug("User:{} 盘点结果画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("enterFlag", "view");
                StocktakeProcessDTO stocktakeProcessDTO = taskService.getStocktake(recordCd);
                //参数为空跳转出错页面
                if(stocktakeProcessDTO==null){
                    mv.setViewName("/error");
                }
                mv.addObject("piCdParam", stocktakeProcessDTO.getPiCd());
                mv.addObject("piDateParam", stocktakeProcessDTO.getPiDate());
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TAKE_STOCK);
                mv.addObject("useMsg", "盘点结果画面");
                break;
            case ConstantsAudit.TYPE_ORDER_RETURN_WAREHOUSE://仓库退货
                log.debug("User:{} 仓库退货管理查看画面", u.getUserId());
                mv.addObject("flag","0");//查看
                mv.addObject("orderId",recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_WAREHOUSE);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_RETURN_WAREHOUSE);
                mv.addObject("useMsg", "仓库退货管理查看画面");
                break;
            case ConstantsAudit.TYPE_ORDER_RETURN_VENDOR://供应商退货
                log.debug("User:{} 供应商退货管理查看画面", u.getUserId());
                mv.addObject("flag","0");//查看
                mv.addObject("orderId",recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_VENDOR);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_RETURN_VENDOR);
                mv.addObject("useMsg", "供应商退货管理查看画面");
                break;
            case ConstantsAudit.TYPE_FS_STOCK://FS库存
                log.debug("User:{} FS库存查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("enterFlag", "view");
                mv.addObject("piCdParam", recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_FS_STOCK);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_FS_STOCK);
                mv.addObject("useMsg", "FS库存查看画面");
                break;
            case ConstantsAudit.TYPE_CUST_STOCK://费用录入
                log.debug("User:{} 费用录入查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("enterFlag", "view");
                mv.addObject("piCdParam", recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_CUST_STOCK);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_CUST_STOCK);
                mv.addObject("useMsg", "费用录入查看画面");
                break;
            case ConstantsAudit.TYPE_MATERIAL_STOCK://原材料库存录入
                log.debug("User:{} 原材料库存查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("enterFlag", "view");
                mv.addObject("piCdParam", recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_MATERIAL_STOCK);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_MATERIAL_STOCK);
                mv.addObject("useMsg", "原材料库存查看画面");
                break;
            case ConstantsAudit.TYPE_RECEIPT_WAREHOUSE://收货 （仓库）
                log.debug("User:{} 收货(仓库)查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("orderSts", "view");
                WarehouseReceiptGridDTO warehouseReceiptGridDTO = taskService.getReceive(recordCd);
                //参数为空跳转出错页面
                if(warehouseReceiptGridDTO==null){
                    mv.setViewName("/error");
                }
                mv.addObject("type", "1");
                mv.addObject("storeCd", warehouseReceiptGridDTO.getStoreCd());
                mv.addObject("orderId", warehouseReceiptGridDTO.getOrderId());
                mv.addObject("receiveId", warehouseReceiptGridDTO.getReceiveId());
                mv.addObject("typeId", ConstantsAudit.TYPE_RECEIPT_WAREHOUSE);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE);
                mv.addObject("useMsg", "收货(仓库)查看画面");
                break;
            case ConstantsAudit.TYPE_RECEIPT_VENDOR://收货 （供应商）
                log.debug("User:{} 收货(供应商)查看画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("orderSts", "view");
                WarehouseReceiptGridDTO warehouseReceiptGridDTO1 = taskService.getReceive(recordCd);
                //参数为空跳转出错页面
                if(warehouseReceiptGridDTO1==null){
                    mv.setViewName("/error");
                }
                mv.addObject("type", "1");
                mv.addObject("storeCd", warehouseReceiptGridDTO1.getStoreCd());
                mv.addObject("orderId", warehouseReceiptGridDTO1.getOrderId());
                mv.addObject("receiveId", warehouseReceiptGridDTO1.getReceiveId());
                mv.addObject("typeId", ConstantsAudit.TYPE_RECEIPT_WAREHOUSE);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE);
                mv.addObject("useMsg", "收货(供应商)查看画面");
                break;
            case ConstantsAudit.TYPE_CASHIER_AMOUNT:// 收银员收款登录
                log.debug("User:{} 收银员收款登录画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("viewSts", "view");
                CashDetail _dto = taskService.getCashier(recordCd);
                //参数为空跳转出错页面
                if(_dto==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data",_dto);
                mv.addObject("typeId", ConstantsAudit.TYPE_CASHIER_AMOUNT);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_CASHIER_AMOUNT);
                mv.addObject("useMsg", "收银员收款登录画面");
                break;
            case ConstantsAudit.TYPE_REC_RET_CORRECTION:// 收货退货更正登录
                log.debug("User:{} 收货退货更正登录画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("orderId", recordCd);
                mv.addObject("viewSts", "view");
                String storeCd = taskService.getStoreCdByOrderId(recordCd);
                //参数为空跳转出错页面
                if(StringUtils.isBlank(storeCd)){
                    mv.setViewName("/error");
                }
                mv.addObject("storeCd", storeCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_REC_RET_CORRECTION);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_REC_RET_CORRECTION);
                mv.addObject("useMsg", "收货退货更正登录画面");
                break;
            case ConstantsAudit.TYPE_TRANSFER_CORRECTION:// 店间转移更正登录
                log.debug("User:{} 店间转移更正登录画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("viewSts", "view");
                Sk0010DTO sk0010DTO = taskService.getSk0010Info(recordCd);
                //参数为空跳转出错页面
                if(sk0010DTO==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", sk0010DTO);
                mv.addObject("typeId", ConstantsAudit.TYPE_TRANSFER_CORRECTION);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_TRANSFER_CORRECTION);
                mv.addObject("useMsg", "店间转移更正登录画面");
                break;
            case ConstantsAudit.TYPE_EXPENDITURE:// 支出登录
                log.debug("User:{} 店间转移更正登录画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                mv.addObject("viewSts", "view");
                ExpenditureParamDTO expenditureInfo = taskService.getExpenditureInfo(recordCd);
                //参数为空跳转出错页面
                if(expenditureInfo==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", expenditureInfo);
                mv.addObject("typeId", ConstantsAudit.TYPE_EXPENDITURE);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_EXPENDITURE);
                mv.addObject("useMsg", "支出登录画面");
                break;
            case ConstantsAudit.TYPE_RETURN_ENTRY_WAREHOUSE:// 仓库退货确认
                log.debug("User:{} 仓库退货确认登录画面", u.getUserId());
                mv.addObject("flag","0");
                mv.addObject("orderId",recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_WAREHOUSE);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_RETURN_ENTRY_WAREHOUSE);
                mv.addObject("useMsg", "仓库退货确认登录画面");
                break;
            case ConstantsAudit.TYPE_RETURN_ENTRY_VENDOR:// 供应商退货确认
                log.debug("User:{} 供应商退货确认登录画面", u.getUserId());
                mv.addObject("flag","0");
                mv.addObject("orderId",recordCd);
                mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_VENDOR);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_RETURN_ENTRY_VENDOR);
                mv.addObject("useMsg", "供应商退货确认登录画面");
                break;
            case ConstantsAudit.TYPE_POG_MNG:// POG管理
                log.debug("User:{} 订货店铺与货品关系画面", u.getUserId());
                Ma1000 ma1000 = returnsDailyService.selectByStoreCd(recordCd);
                mv.addObject("storeCd",ma1000.getStoreCd());
                mv.addObject("storeName",ma1000.getStoreName());
                mv.addObject("typeId", ConstantsAudit.TYPE_POG_MNG);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_POG_MNG);
                mv.addObject("useMsg", "订货店铺与货品关系画面");
                break;
            case ConstantsAudit.TYPE_ORDER_ITEM_TRANSFER://店内商品调拨
                log.debug("User:{} 店内商品调拨详情画面", u.getUserId());
                mv.addObject("use", 0);
                mv.addObject("identity", 1);
                Sk0010DTO adjustItem = taskService.getStockScrap(recordCd);
                //参数为空跳转出错页面
                if(adjustItem==null){
                    mv.setViewName("/error");
                }
                mv.addObject("data", adjustItem);
                mv.addObject("viewSts", "view");
                mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_ITEM_TRANSFER);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_ITEM_TRANSFER);
                mv.addObject("useMsg", "店内商品调拨详情画面");
                break;
            case ConstantsAudit.TYPE_PLAN_STOCK:// 盘点计划
                log.debug("User:{} 盘点计划画面", u.getUserId());
                StocktakeProcessDTO stocktakeProcessDTO1 = taskService.getStocktake(recordCd);
                //参数为空跳转出错页面
                if(stocktakeProcessDTO1==null){
                    mv.setViewName("/error");
                }
                String businessDate = cm9060Service.getValByKey("0000");
                mv.addObject("use", 0);
                mv.addObject("identity", "1");
                mv.addObject("enterFlag","view"); // 操作状态
                mv.addObject("businessDate",businessDate);
                mv.addObject("piCdParam", stocktakeProcessDTO1.getPiCd());
                mv.addObject("piDateParam", stocktakeProcessDTO1.getPiDate());
                mv.addObject("typeId", ConstantsAudit.TYPE_PLAN_STOCK);
                mv.addObject("reviewId", ConstantsAudit.REVIEW_PLAN_STOCK);
                mv.addObject("useMsg", "盘点计划画面");
                break;
            default:
                mv.setViewName("/error");
            break;
        }
        this.saveToken(request);
        return mv;
    }
}
