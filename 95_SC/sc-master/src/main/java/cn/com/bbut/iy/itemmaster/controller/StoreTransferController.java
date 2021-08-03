package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoBean;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ElectronicReceiptParam;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0060;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma1200Service;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.service.inventoryQtyCor.AllRtInCorrQtyService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 店间调拨
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeTransfers")
public class StoreTransferController extends BaseAction {

    @Autowired
    private InventoryVouchersService service;
    @Autowired
    private InventoryVouchersMapper inventoryVouchersMapper;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    protected INotificationService notificationServiceImpl;
    @Autowired
    protected IReviewService reviewServiceImpl;
    @Autowired
    private AllRtInCorrQtyService allRtService;
    @Autowired
    private RealTimeInventoryQueryService rtService;
    @Autowired
    private Ma1200Service ma1200Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_STORE_TRANSFER_OUT";
    private final String EXCEL_EXPORT_NAME = "Store Transfer Out Query.xlsx";

    /**
     * 店间调拨管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFEROUT_LIST_VIEW})
    public ModelAndView storeTransfersList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调出一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storetransfers/storetransfers");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFEROUT);
        mv.addObject("useMsg", "店间调出一览画面");
        return mv;
    }

    /**
     * 店间调拨管理详情查看画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_TRANSFEROUT_VIEW,
            PermissionCode.CODE_SC_ST_KCCPYL_VIEW
    })
    public ModelAndView view(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010,String stsName) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调出管理详情查看画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storetransfers/storetransfersedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("stsName", stsName);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFEROUT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFEROUT);
        mv.addObject("useMsg", "店间调出管理详情查看画面");
        this.saveToken(request);
        return mv;
    }
    @RequestMapping(value = "/SouthOrNorth", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO  getSouthOrNorth(String storeCd){
        if(StringUtils.isNotBlank(storeCd)){
            Ma1000DTO southOrNouth = service.getSouthOrNouth(storeCd);
            return  new ReturnDTO(true,"success",southOrNouth);
        }
        return  new ReturnDTO(false,"check store fail");

    }
    /**
     * 店间调拨管理详情编辑、新增画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_TRANSFEROUT_ADD,
            PermissionCode.CODE_SC_ST_TRANSFEROUT_EDIT,
            PermissionCode.CODE_SC_ST_KCCPYL_EDIT
    })
    public ModelAndView edit(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010, String flag,String stsName) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调出管理详情编辑、新增画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storetransfers/storetransfersedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", flag);
        mv.addObject("stsName", stsName);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFEROUT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFEROUT);
        mv.addObject("useMsg", "店间调出管理详情编辑、新增画面");
        this.saveToken(request);
        return mv;
    }





    /**
     * 店间调拨管理商品详情更新
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update")
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFEROUT_EDIT })
    public ReturnDTO updateDetails(HttpServletRequest request, HttpSession session, String listJson, String flg) {
        ReturnDTO _return = new ReturnDTO();
        if(!"1".equals(flg)){
            // 未修改数量，不执行更新
            _return.setMsg("Don't need to update data!");
            _return.setSuccess(true);
            return _return;
        }
        if(StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 执行保存
        int i = service.updateSk0020(dto, sk0020List);
        if(i < 1){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFEROUT_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ST_TRANSFEROUT_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("storeTransferOutExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 获取当前操作用户、时间
     */
    private CommonDTO getCommonDTO(HttpSession session){
        User u = this.getUser(session);
        if(u == null){
            return null;
        }
        CommonDTO dto = new CommonDTO();
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(ymd);
        dto.setUpdateYmd(ymd);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(hms);
        dto.setUpdateHms(hms);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,InventoryVouchersParamDTO param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
                && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(StringUtils.isNotBlank(param.getStoreCd())){
            stores.add(param.getStoreCd());
            return stores;
        }
        // 只选择了一部分参数，生成查询参数，后台查询判断
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

    /**
     * 更新审核信息
     * @param
     * @return
     */
    @RequestMapping("/submit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto submitStep(long auditStepId, String auditUserId, String auditStatus, String auditContent, String detailType,
                                    HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        User user = this.getUser(session);
        AjaxResultDto _return = new AjaxResultDto();

        if(auditStatus == null){
            _return.setSuccess(false);
            _return.setMessage("Param");
            log.info("-------------------------auditStatus"+auditStatus+"auditStatus为空-------------------------");
            return _return;
        }
        try {
            // from t_audit_tab
            AuditBean auditStep = auditServiceImpl.getAuditById(auditStepId);
            if(auditStep == null){
                _return.setSuccess(false);
                _return.setMessage("Null");
                log.error("-------------------------审核记录ID,在t_audit_tab没查询出审核记录-------------------------");
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
                log.error("-------------------------review type error,t_audit_tab中typeid == 0-------------------------");
                return _return;
            }
            // 记录审核结果
            String _flag = "0";

            if("1".equals(auditStatus)){//审核通过
                long subMax = reviewServiceImpl.getSubMaxByReviewId(reviewid);
                long nowSubNo = auditStep.getSubNo();

                if(subMax == nowSubNo){
                    auditStep.setCAuditstatus(99);
                    if(detailType != null){
                        boolean checkFlg = reNewalMsg(detailType,auditStep.getStoreCd(),auditStep.getCRecordCd());
                        if(!checkFlg){
                            _return.setSuccess(false);
                            _return.setMessage("Failed to save inventory information!");
                            log.error("-------------------------Failed to save inventory information!-------------------------");
                            return _return;
                        }
                    }
                    auditServiceImpl.updateAuditStatus(auditStep);
                    _flag = "10";
                }else if(subMax > nowSubNo){
                    flag = true;

                }else{
                    _return.setSuccess(false);
                    _return.setMessage("review step error");
                    log.error("-------------------------review step error-------------------------");
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
                    SK0010Key sk0010 = new SK0010Key();
                    sk0010.setVoucherNo(recordCd);
                    SK0010 _dto = service.getSk0010(sk0010);
                    // 生成新的流程
                    AuditBean newAuditBean = new AuditBean();
                    // 审核流程ID
                    long reviewId = auditStep.getNReviewid();
                    newAuditBean.setNReviewid(reviewId);
                    // 类别ID
                    newAuditBean.setDistinguishId(auditStep.getDistinguishId());
                    // 主档类型ID
                    newAuditBean.setNTypeid(typeId);
                    if(subNo>=1){
                        // 店铺cd
                        newAuditBean.setStoreCd(_dto.getStoreCd1());
                    }else {
                        // 店铺cd
                        newAuditBean.setStoreCd(storeCd);
                    }
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
                        log.debug("-------------------------新流程生成成功,生成流程的通知-------------------------");
                        // 新建通知
                        NotificationBean notificationBean = new NotificationBean();
                        // 店铺cd
                        if(subNo>=1){
                            notificationBean.setStoreCd(_dto.getStoreCd1());
                        }else {
                            notificationBean.setStoreCd(storeCd);
                        }
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

                        int addNotification = notificationServiceImpl.addNotification(notificationBean);
                        if(addNotification > 0){
                            log.debug("-------------------------审核更新 + 生成新流程、通知 OK-------------------------");
                        }
                    }
                }else{
                    _return.setMessage("Initiate audit failure!");
                    log.error("------------------------- Initiate audit failure! -------------------------");
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
            case "tmp_transfer_out":
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

        }
        log.info("修正实时库存和bi库存保存失败");
        return false;
    }

    // inventory下修正库存共通
    protected boolean checkReNewMsg(String detailType,String storeCd,List<Sk0020DTO> _sk0020List){
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

    @RequestMapping(value = "/toPrint")
    @ResponseBody
    public ReturnDTO fileToPdf(HttpSession session, String searchJson, OutputStream outputStream) {

        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010Key sk0010 = gson.fromJson(searchJson, SK0010Key.class);
        if(sk0010 == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            sk0020 = new Sk0020ParamDTO();
        }

        SK0010 _dto = service.getSk0010(sk0010);
        List<Sk0020DTO> _list = inventoryVouchersMapper.selectListSk0020OutStore(sk0020);
        _dto.setList(_list);
        if(_dto == null || _list==null){
            _return.setMsg("Query failed!");
        }else {
            _return.setMsg("Query succeeded!");
            _return.setO(gson.toJson(_dto));
            _return.setSuccess(true);
        }
        return _return;
    }

    @RequestMapping(value = "/writerPdf")
    public void fileAndPdf(HttpSession session, HttpServletResponse response,String searchJson){
        if(StringUtils.isBlank(searchJson)){
            return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010Key sk0010 = gson.fromJson(searchJson, SK0010Key.class);
        if(sk0010 == null){
            return;
        }
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            sk0020 = new Sk0020ParamDTO();
        }

        SK0010 _dto = service.getSk0010(sk0010);
        List<Sk0020DTO> _list = inventoryVouchersMapper.selectListSk0020OutStore(sk0020);
        _dto.setList(_list);
        if(_dto == null || _list==null){
            return;
        }

        String fileName = "Store Transfer Entry.pdf";
        float lineHeight1 = (float)25.0;
        float lineHeight2 = (float)20.0;
        float lineHeight3 = (float)15.0;
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);

            OutputStream outputStream = response.getOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            // 页面大小(A4纵向)
            Rectangle rectangle = new Rectangle(new RectangleReadOnly(595F, 842F));
            // 页面背景颜色
            rectangle.setBackgroundColor(BaseColor.WHITE);
            document.setPageSize(rectangle);
            // 页边距 左，右，上，下
            document.setMargins(5, 5, 5, 5);
            document.open();
            //设置字体
            //非汉字字体颜色
            Font f0 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14f);
            f0.setColor(BaseColor.BLACK);
            f0.setStyle(Font.BOLD);

            Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10f);
            f1.setColor(BaseColor.BLACK);
            f1.setStyle(Font.BOLD);


//            List<Sk0020DTO> _list = _dto.getList();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10f);
            f2.setColor(BaseColor.BLACK);

            Font f3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10f);
            f3.setColor(BaseColor.BLACK);
            f3.setStyle(Font.BOLD);

            Paragraph p = new Paragraph();
            PdfPTable onTable = new PdfPTable(5);
            // 标题
            p = new Paragraph("STORE TO STORE TRANSFER",f0);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell firstCell = new PdfPCell(p);
            firstCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            firstCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            firstCell.setFixedHeight(lineHeight1);
            firstCell.setColspan(5);
            firstCell.setBorderColor(BaseColor.WHITE);
            onTable.addCell(firstCell);

            // 第二行
            String str = _dto.getVoucherNo();
            String content = "Document ID:  "+str;
            p = new Paragraph(content,f1);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell secCell = new PdfPCell(p);
            secCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            secCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            secCell.setFixedHeight(lineHeight1);
            secCell.setColspan(5);
            secCell.setBorderColor(BaseColor.WHITE);
            onTable.addCell(secCell);

            p = new Paragraph("",f2);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell noCell = new PdfPCell(p);
            noCell.setColspan(5);
            noCell.setBorderColor(BaseColor.WHITE);
            onTable.addCell(noCell);

            // 第三行
            // 获取审核时间
            String str1 = "System Transfer Date";
            p = new Paragraph(str1,f2);
            PdfPCell thirdCell1 = new PdfPCell(p);
            thirdCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            thirdCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            thirdCell1.setFixedHeight(lineHeight2);
            thirdCell1.setColspan(2);
            thirdCell1.setBorderColor(BaseColor.WHITE);
            onTable.addCell(thirdCell1);

            String str2 = "";
            String approvalDateTime = _dto.getUpdateYmd();
            if(approvalDateTime != null || approvalDateTime.length() == 14){
                str2 = dateFomte(approvalDateTime);
            }
            p = new Paragraph(str2,f2);
            p.setAlignment(Element.ALIGN_LEFT);
            PdfPCell thirdCell2 = new PdfPCell(p);
            thirdCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            thirdCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            thirdCell2.setFixedHeight(lineHeight2);
            thirdCell2.setBorderColor(BaseColor.WHITE);
            thirdCell2.setColspan(3);
            onTable.addCell(thirdCell2);
            // 第四行
            p = new Paragraph("From Store",f2);
            PdfPCell fourthCell1 = new PdfPCell(p);
            fourthCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            fourthCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fourthCell1.setFixedHeight(lineHeight2);
            fourthCell1.setColspan(2);
            fourthCell1.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fourthCell1);

            SK0010 fromStore = service.getStoreSeries(_dto.getStoreCd());
            p = new Paragraph(fromStore.getStoreCd(),f2);
            PdfPCell fourthCell2 = new PdfPCell(p);
            fourthCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            fourthCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fourthCell2.setFixedHeight(lineHeight2);
            fourthCell2.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fourthCell2);

            p = new Paragraph(fromStore.getStoreName()+", "+fromStore.getDistrictName()+", "+fromStore.getCityName(),f2);
            PdfPCell fourthCell3 = new PdfPCell(p);
            fourthCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            fourthCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fourthCell3.setFixedHeight(lineHeight2);
            fourthCell3.setColspan(2);
            fourthCell3.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fourthCell3);

            // 第五行
            p = new Paragraph("To Store",f2);
            PdfPCell fifthCell1 = new PdfPCell(p);
            fifthCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            fifthCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fifthCell1.setFixedHeight(lineHeight2);
            fifthCell1.setColspan(2);
            fifthCell1.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fifthCell1);

            SK0010 toStore = service.getStoreSeries(_dto.getStoreCd1());
            p = new Paragraph(toStore.getStoreCd(),f2);
            PdfPCell fifthCell2 = new PdfPCell(p);
            fifthCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            fifthCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fifthCell2.setFixedHeight(lineHeight2);
            fifthCell2.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fifthCell2);

            p = new Paragraph(toStore.getStoreName()+", "+toStore.getDistrictName()+", "+toStore.getCityName(),f2);
            PdfPCell fifthCell3 = new PdfPCell(p);
            fifthCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            fifthCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fifthCell3.setFixedHeight(lineHeight2);
            fifthCell3.setColspan(2);
            fifthCell3.setBorderColor(BaseColor.WHITE);
            onTable.addCell(fifthCell3);
            document.add(onTable);

            PdfPTable downTable = new PdfPTable(9);
            // 第一行
            PdfPCell downth1Cell1 = new PdfPCell(new Paragraph("No.",f2));
            downth1Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth1Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell1.setFixedHeight(lineHeight2);
            downth1Cell1.setBorderColor(BaseColor.BLACK);
            downth1Cell1.setBorderWidthRight(0);
            downth1Cell1.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell1);

            PdfPCell downth1Cell2 = new PdfPCell(new Paragraph("Item Code",f2));
            downth1Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            downth1Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell2.setFixedHeight(lineHeight2);
            downth1Cell2.setBorderColor(BaseColor.BLACK);
            downth1Cell2.setBorderWidthLeft(0);
            downth1Cell2.setBorderWidthRight(0);
            downth1Cell2.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell2);

            PdfPCell downth1Cell3 = new PdfPCell(new Paragraph("Item Name",f2));
            downth1Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth1Cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell3.setFixedHeight(lineHeight2);
            downth1Cell3.setColspan(3);
            downth1Cell3.setBorderWidthLeft(0);
            downth1Cell3.setBorderWidthRight(0);
            downth1Cell3.setBorderColor(BaseColor.BLACK);
            downth1Cell3.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell3);

            PdfPCell downth1Cell4 = new PdfPCell(new Paragraph("Barcode",f2));
            downth1Cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            downth1Cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell4.setFixedHeight(lineHeight2);
            downth1Cell4.setBorderColor(BaseColor.BLACK);
            downth1Cell4.setColspan(2);
            downth1Cell4.setBorderWidthLeft(0);
            downth1Cell4.setBorderWidthRight(0);
            downth1Cell4.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell4);

            PdfPCell downth1Cell5 = new PdfPCell(new Paragraph("UOM",f2));
            downth1Cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth1Cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell5.setFixedHeight(lineHeight2);
            downth1Cell5.setBorderColor(BaseColor.BLACK);
            downth1Cell5.setBorderWidthLeft(0);
            downth1Cell5.setBorderWidthRight(0);
            downth1Cell5.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell5);

            PdfPCell downth1Cell6 = new PdfPCell(new Paragraph("Quantity",f2));
            downth1Cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth1Cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            downth1Cell6.setFixedHeight(lineHeight2);
            downth1Cell6.setBorderColor(BaseColor.BLACK);
            downth1Cell6.setBorderWidthLeft(0);
            downth1Cell6.setBackgroundColor(BaseColor.GRAY);
            downTable.addCell(downth1Cell6);

            int totalQty = 0;
            int num = 0;
            for (int i = 0; i < _list.size(); i++) {
                num++;
                // No.
                PdfPCell downthCell1 = new PdfPCell(new Paragraph(String.valueOf(num)));
                downthCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                downthCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell1.setFixedHeight(lineHeight2);
                downthCell1.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell1);
                // item code
                PdfPCell downthCell2 = new PdfPCell(new Paragraph(_list.get(i).getArticleId(),f2));
                downthCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                downthCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell2.setFixedHeight(lineHeight2);
                downthCell2.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell2);
                // item name
                PdfPCell downthCell3 = new PdfPCell(new Paragraph(_list.get(i).getArticleName1(),f2));
                downthCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                downthCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell3.setFixedHeight(lineHeight2);
                downthCell3.setColspan(3);
                downthCell3.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell3);
                // barcode
                PdfPCell downthCell4 = new PdfPCell(new Paragraph(_list.get(i).getBarcode(),f2));
                downthCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                downthCell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell4.setFixedHeight(lineHeight2);
                downthCell4.setColspan(2);
                downthCell4.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell4);
                // UOM
                PdfPCell downthCell5 = new PdfPCell(new Paragraph(_list.get(i).getSalesUnitId(),f2));
                downthCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                downthCell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell5.setFixedHeight(lineHeight2);
                downthCell5.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell5);
                // qty
                PdfPCell downthCell6 = new PdfPCell(new Paragraph(String.valueOf(_list.get(i).getQty1().intValue()),f2));
                downthCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                downthCell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                downthCell6.setFixedHeight(lineHeight2);
                downthCell6.setBorderColor(BaseColor.WHITE);
                downTable.addCell(downthCell6);
                // 合计数量
                totalQty += _list.get(i).getQty1().intValue();
            }
            PdfPCell downth2Cell1 = new PdfPCell(new Paragraph("TOTAL",f2));
            downth2Cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth2Cell1.setVerticalAlignment(Element.ALIGN_TOP);
            downth2Cell1.setFixedHeight(lineHeight1);
            downth2Cell1.setBorderColor(BaseColor.BLACK);
            downth2Cell1.setBorderWidthBottom(0);
            downth2Cell1.setBorderWidthLeft(0);
            downth2Cell1.setBorderWidthRight(0);
            downTable.addCell(downth2Cell1);

            PdfPCell downth2Cell2 = new PdfPCell(new Paragraph(String.valueOf(_list.size()),f2));
            downth2Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            downth2Cell2.setVerticalAlignment(Element.ALIGN_TOP);
            downth2Cell2.setFixedHeight(lineHeight1);
            downth2Cell2.setBorderColor(BaseColor.BLACK);
            downth2Cell2.setColspan(7);
            downth2Cell2.setBorderWidthBottom(0);
            downth2Cell2.setBorderWidthLeft(0);
            downth2Cell2.setBorderWidthRight(0);
            downTable.addCell(downth2Cell2);

            PdfPCell downth2Cell3 = new PdfPCell(new Paragraph(String.valueOf(totalQty),f2));
            downth2Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            downth2Cell3.setVerticalAlignment(Element.ALIGN_TOP);
            downth2Cell3.setFixedHeight(lineHeight1);
            downth2Cell3.setBorderColor(BaseColor.BLACK);
            downth2Cell3.setBorderWidthBottom(0);
            downth2Cell3.setBorderWidthLeft(0);
            downth2Cell3.setBorderWidthRight(0);
            downTable.addCell(downth2Cell3);
            document.add(downTable);


            PdfPTable bottomTable = new PdfPTable(2);
            p = new Paragraph("Transfer OUT by Store",f3);

            PdfPCell bott1Cell1 = new PdfPCell(p);
            bott1Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott1Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott1Cell1.setFixedHeight(lineHeight3);
            bott1Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott1Cell1);

            p = new Paragraph("Transfer IN by Store",f3);
            PdfPCell bott1Cell2 = new PdfPCell(p);
            bott1Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott1Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott1Cell2.setFixedHeight(lineHeight3);
            bott1Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott1Cell2);
            // 获取审核信息
            List<SK0010> auditInfo = service.getApprovedInfo(_dto.getVoucherNo());
            log.info("<<<<审核信息数量:"+auditInfo.size());
            if(auditInfo.size()<4){
                log.error("<<<<审核信息数量不足:"+auditInfo.size());
            }

            p = new Paragraph("Store Manager:",f3);
            PdfPCell bott2Cell1 = new PdfPCell(p);
            bott2Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott2Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott2Cell1.setFixedHeight(lineHeight3);
            bott2Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott2Cell1);

            PdfPCell bott2Cell2 = new PdfPCell(p);
            bott2Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott2Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott2Cell2.setFixedHeight(lineHeight3);
            bott2Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott2Cell2);

            // 转出store SM
            p = new Paragraph(auditInfo.get(0).getNOperatorName(),f2);
            PdfPCell bott3Cell1 = new PdfPCell(p);
            bott3Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott3Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott3Cell1.setFixedHeight(lineHeight3);
            bott3Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott3Cell1);

            // 转入store SM
            p = new Paragraph(auditInfo.get(2).getNOperatorName(),f2);
            PdfPCell bott3Cell2 = new PdfPCell(p);
            bott3Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott3Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott3Cell2.setFixedHeight(lineHeight3);
            bott3Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott3Cell2);

            p = new Paragraph("Approved Date:",f3);
            PdfPCell bott4Cell1 = new PdfPCell(p);
            bott4Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott4Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott4Cell1.setFixedHeight(lineHeight3);
            bott4Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott4Cell1);

            PdfPCell bott4Cell2 = new PdfPCell(p);
            bott4Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott4Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott4Cell2.setFixedHeight(lineHeight3);
            bott4Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott4Cell2);

            String auditTime = "";
            // 转出store SM 审核时间
            if(auditInfo.get(0).getAuditTime() != null || auditInfo.get(0).getAuditTime().length() == 14){
                auditTime = dateFomte(auditInfo.get(0).getAuditTime());
            }
            p = new Paragraph(auditTime,f2);
            PdfPCell bott5Cell1 = new PdfPCell(p);
            bott5Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott5Cell1.setVerticalAlignment(Element.ALIGN_TOP);
            bott5Cell1.setFixedHeight(lineHeight1);
            bott5Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott5Cell1);

            // 转入store SM  审核时间
            if(auditInfo.get(0).getAuditTime() != null || auditInfo.get(0).getAuditTime().length() == 14){
                auditTime = dateFomte(auditInfo.get(0).getAuditTime());
            }else {
                auditTime = "";
            }
            p = new Paragraph(auditTime,f2);
            PdfPCell bott5Cell2 = new PdfPCell(p);
            bott5Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott5Cell2.setVerticalAlignment(Element.ALIGN_TOP);
            bott5Cell2.setFixedHeight(lineHeight1);
            bott5Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott5Cell2);

            // -- AM
            p = new Paragraph("Area Manager:",f3);
            PdfPCell bott6Cell1 = new PdfPCell(p);
            bott6Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott6Cell1.setVerticalAlignment(Element.ALIGN_BOTTOM);
            bott6Cell1.setFixedHeight(lineHeight1);
            bott6Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott6Cell1);

            PdfPCell bott6Cell2 = new PdfPCell(p);
            bott6Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott6Cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
            bott6Cell2.setFixedHeight(lineHeight1);
            bott6Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott6Cell2);

            // 转出store AM
            p = new Paragraph(auditInfo.get(1).getNOperatorName(),f2);
            PdfPCell bott7Cell1 = new PdfPCell(p);
            bott7Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott7Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott7Cell1.setFixedHeight(lineHeight3);
            bott7Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott7Cell1);

            // 转入store AM
            p = new Paragraph(auditInfo.get(3).getNOperatorName(),f2);
            PdfPCell bott7Cell2 = new PdfPCell(p);
            bott7Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott7Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott7Cell2.setFixedHeight(lineHeight3);
            bott7Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott7Cell2);

            p = new Paragraph("Approved Date:",f3);
            PdfPCell bott8Cell1 = new PdfPCell(p);
            bott8Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott8Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott8Cell1.setFixedHeight(lineHeight3);
            bott8Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott8Cell1);

            PdfPCell bott8Cell2 = new PdfPCell(p);
            bott8Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott8Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott8Cell2.setFixedHeight(lineHeight3);
            bott8Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott8Cell2);

            String amAuditTime = "";
            // 转出store AM 审核时间
            if(auditInfo.get(1).getAuditTime() != null || auditInfo.get(1).getAuditTime().length() == 14){
                amAuditTime = dateFomte(auditInfo.get(1).getAuditTime());
            }
            p = new Paragraph(amAuditTime,f2);
            PdfPCell bott9Cell1 = new PdfPCell(p);
            bott9Cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott9Cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott9Cell1.setFixedHeight(lineHeight3);
            bott9Cell1.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott9Cell1);

            // 转入store AM  审核时间
            if(auditInfo.get(3).getAuditTime() != null || auditInfo.get(3).getAuditTime().length() == 14){
                amAuditTime = dateFomte(auditInfo.get(3).getAuditTime());
            }else {
                amAuditTime = "";
            }
            p = new Paragraph(amAuditTime,f2);
            PdfPCell bott9Cell2 = new PdfPCell(p);
            bott9Cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            bott9Cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            bott9Cell2.setFixedHeight(lineHeight3);
            bott9Cell2.setBorderColor(BaseColor.WHITE);
            bottomTable.addCell(bott9Cell2);

            document.add(bottomTable);

            document.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.info("<<<<<"+"导出pdf异常");
            e.printStackTrace();
        } catch (DocumentException e) {
            log.info("<<<<<"+"文件异常");
            e.printStackTrace();
        }
    }

    private String dateFomte(String dateTime){
        String year=dateTime.substring(0,4);
        String month=dateTime.substring(4,6);
        String day=dateTime.substring(6,8);
        String hour=dateTime.substring(8,10);
        String min=dateTime.substring(10,12);
        String sec=dateTime.substring(12,14);
        return day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec;
    }
}
