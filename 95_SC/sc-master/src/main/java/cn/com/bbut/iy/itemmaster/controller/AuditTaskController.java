package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.audit.AuditController;
import cn.com.bbut.iy.itemmaster.controller.audit.TypeIdTools;
import cn.com.bbut.iy.itemmaster.dao.CustEntryMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.audit.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVHeadResult;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.service.inventoryQtyCor.AllRtInCorrQtyService;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private AllRtInCorrQtyService allRtService;
    @Autowired
    private RealTimeInventoryQueryService rtService;
    @Autowired
    private Ma1200Service ma1200Service;
    @Autowired
    private CustEntryMapper custEntryMapper;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private InventoryVouchersService iVService;

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
                String _type = "";
                if(typeId==16){
                    String reTableName = "od0000";
                    _type = auditServiceImpl.getCorrType(reTableName,recordCd,typeId);
                }else if(typeId==17){
                    String reTableName = "sk0010";
                    _type = auditServiceImpl.getCorrType(reTableName,recordCd,typeId);
                }
                String detailType = TypeIdTools.getDetailType(typeId,_type);

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

                        boolean checkFlg = reNewalMsg(detailType,auditStep.getStoreCd(),recordCd);
                        if(!checkFlg){
                            ard.setSuccess(false);
                            ard.setMessage("Failed to save inventory information!");
                            return ard;
                        }
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
                String storeCd = null;
                if(record != null){
                    log.debug("-------------------------已进行审核更新，取消通知显示-------------------------");
                    record.setStatus(_flag);
                    storeCd = record.getEffectiveStartDate();
                    notificationServiceImpl.updateNotification(record);
                }
                if(r > 0){
                    if(flag){
                        log.debug("-------------------------步骤更新完成，生成流程下一步-------------------------");
                        SK0010 _dto = new SK0010();
                        if(auditStep.getNReviewid() == ConstantsAudit.REVIEW_ORDER_TRANSFEROUT){
                            SK0010Key sk0010 = new SK0010Key();
                            sk0010.setVoucherNo(recordCd);
                            _dto = iVService.getSk0010(sk0010);
                        }

                        // 生成新的流程
                        AuditBean newAuditBean = new AuditBean();
                        // 审核流程ID
                        long reviewId = auditStep.getNReviewid();
                        newAuditBean.setNReviewid(reviewId);
                        // 类别ID
                        newAuditBean.setDistinguishId(auditStep.getDistinguishId());
                        // 主档类型ID
                        newAuditBean.setNTypeid(typeId);
                        if(auditStep.getNReviewid() == ConstantsAudit.REVIEW_ORDER_TRANSFEROUT && subNo>=1){
                            newAuditBean.setStoreCd(_dto.getStoreCd1());
                        }else {
                            newAuditBean.setStoreCd(storeCd);
                        }

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
                            // 店铺
                            if(auditStep.getNReviewid() == ConstantsAudit.REVIEW_ORDER_TRANSFEROUT && subNo>=1){
                                notificationBean.setStoreCd(_dto.getStoreCd1());
                            }else {
                                notificationBean.setStoreCd(storeCd);
                            }

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

    /**
     * 修正实时库存和bi库存
     * @param detailType
     * @param storeCd
     * @param recordCd
     * @return
     */
    private boolean reNewalMsg(String detailType, String storeCd, String recordCd){
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
                checkFlg = checkTransferNewMsg(detailType,storeCd,_sk0020List);
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
            default:
                return true;
        }
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
                String[] str = urlData.split("}");
                if(str.length<=1){
                    Gson gson = new Gson();
                    RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
                    if("500".equals(param.getStatus()) || param.getContent() == null){
                        String message = "Failed to connect to live inventory data！";
                        checkData = false;
                    }
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

    // inventory下店间转移修正库存
    protected boolean checkTransferNewMsg(String detailType,String storeCd,List<Sk0020DTO> _sk0020List){
        boolean checkFlg = true;
        String storeCd1 = "";
        // 判断母货号是否允许操作（Group Sale子母货号是否分开管理  0：分开 1：不分开）
        // String _val = cm9060Service.getValByKey("0634");
        // boolean parentFlg = "0".equals(_val) ? true : false;
        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        List<SaveInventoryQty> saveRtInQtyList = new ArrayList<>();
        if(_sk0020List.size() > 0){
            storeCd = _sk0020List.get(0).getStoreCd();
            storeCd1 = _sk0020List.get(0).getStoreCd1();
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
                // 转入商品信息
                SaveInventoryQty saveDetailInQty = new SaveInventoryQty();
                saveDetailInQty.setDetailType("tmp_transfer_in");
                saveDetailInQty.setStoreCd(sk020.getStoreCd1());
                saveDetailInQty.setArticleId(sk020.getArticleId());
                saveDetailInQty.setInventoryQty(qty1);
                if(saveDetailInQty.getInventoryQty() == 0.0){
                    continue;
                }

                saveRtQtyList.add(saveDetail);
                if(detailType.equals("tmp_transfer_out") ){
                    saveRtInQtyList.add(saveDetailInQty);
                }
                parentList.add(sk020.getArticleId());
            }

            if(saveRtQtyList.size() == 0){
                return true;
            }
            // 修正商品实时库存（修正bi库存时,有可能会修改saveRtQtyList的数量的正负号，因此先进行修正实时库存）
            RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
            if(saveRtInQtyList.size()>0){
                RtInvContent tranferInContent = rtService.saveRtQtyListToEs(saveRtInQtyList);
                if(!tranferInContent.getStatus().equals("0")){
                    checkFlg = false;
                }
            }
            if(!rtInvContent.getStatus().equals("0")){
                checkFlg = false;
            }
            // 修正bi库存
            String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
            if(rtBi == null){
                checkFlg = false;
            }
            if(saveRtInQtyList.size()>0){
                String rtTranferInBi = rtService.saveBIrtQty(saveRtInQtyList,"tmp_transfer_in",storeCd1);
                if(rtTranferInBi == null){
                    checkFlg = false;
                }
            }

            // 判断是否存在group商品母货号
            List<SaveInventoryQty> groupSavertList = new ArrayList<>();
            List<SaveInventoryQty> groupSavertTranInList = new ArrayList<>();
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
                        if(saveRtInQtyList.size()>0) {
                            for (SaveInventoryQty saveInQty : saveRtInQtyList) {
                                if (saveInQty.getArticleId().equals(map.get("parentArticleId"))) {
                                    float qty1 = saveInQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1 * Integer.parseInt(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                    saveGroupDetail .setArticleId(childeArticleId);
                                    saveGroupDetail.setInventoryQty(articleQty);
                                    saveGroupDetail.setDetailType(saveInQty.getDetailType());
                                    saveGroupDetail.setStoreCd(saveInQty.getStoreCd());
                                    groupSavertTranInList.add(saveGroupDetail);
                                }
                            }
                        }
                    }
                }
                // 修正子商品实时库存
                RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                if (!rtChildContent.getStatus().equals("0")) {
                    checkFlg = false;
                }
                if(groupSavertTranInList.size()>0){
                    RtInvContent rtInChildContent = rtService.saveRtQtyListToEs(groupSavertTranInList);
                    if (!rtInChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                }

                // 修正bi库存
                String groupRtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                if(groupRtBi == null){
                    checkFlg = false;
                }
                if(groupSavertTranInList.size()>0){
                    // 修正转入商品bi库存
                    String groupInRtBi = rtService.saveBIrtQty(groupSavertTranInList,"tmp_transfer_in",storeCd1);
                    if(groupInRtBi == null){
                        checkFlg = false;
                    }
                }
            }

            // 判断是否存在BOM商品母货号
            List<SaveInventoryQty> bomSavertList = new ArrayList<>();
            List<SaveInventoryQty> bomSaveTranInrtList = new ArrayList<>();
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
                        if(saveRtInQtyList.size()>0) {
                            for (SaveInventoryQty saveQty : saveRtInQtyList) {
                                if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                    float qty1 = saveQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1*Float.parseFloat(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                    saveBomDetail.setArticleId(childeArticleId);
                                    saveBomDetail.setInventoryQty(articleQty);
                                    saveBomDetail.setDetailType(saveQty.getDetailType());
                                    saveBomDetail.setStoreCd(saveQty.getStoreCd());
                                    bomSaveTranInrtList.add(saveBomDetail);
                                }
                            }
                        }
                    }
                    // 修正子商品实时库存
                    RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                    if (!rtChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                    if(bomSaveTranInrtList.size()>0){
                        // 修正转入数量子商品实时库存
                        RtInvContent rtInChildContent = rtService.saveRtQtyListToEs(bomSaveTranInrtList);
                        if (!rtInChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                    }
                    // 修正bi库存
                    String bomRtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                    if(bomRtBi == null){
                        checkFlg = false;
                    }
                    if(bomSaveTranInrtList.size()>0){
                        // 修正转入商品bi库存
                        String bomInRtBi = rtService.saveBIrtQty(bomSaveTranInrtList,"tmp_transfer_in",storeCd1);
                        if(bomInRtBi == null){
                            checkFlg = false;
                        }
                    }
                }
            }
            return checkFlg;
        }
        return checkFlg;
    }
}
