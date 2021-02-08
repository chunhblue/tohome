//package cn.com.bbut.iy.itemmaster.controller;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import cn.com.bbut.iy.itemmaster.annotation.Permission;
//import cn.com.bbut.iy.itemmaster.annotation.ResultTypeEnum;
//import cn.com.bbut.iy.itemmaster.annotation.Secure;
//import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
//import cn.com.bbut.iy.itemmaster.constant.Constants;
//import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
//import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
//import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
//import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
//import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
//import cn.com.bbut.iy.itemmaster.dto.bm.AjaxResultBmDto;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmCodeDTO;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmExcelParam;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmItemResultDto;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmParamDTO;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmUserInfoDTO;
//import cn.com.bbut.iy.itemmaster.dto.bm.BmViewMainDTO;
//import cn.com.bbut.iy.itemmaster.entity.User;
//import cn.com.bbut.iy.itemmaster.excel.ExService;
//import cn.com.bbut.iy.itemmaster.service.StoreService;
//import cn.com.bbut.iy.itemmaster.service.base.DptService;
//import cn.com.bbut.iy.itemmaster.service.base.IyItemMService;
//import cn.com.bbut.iy.itemmaster.service.base.OccupService;
//import cn.com.bbut.iy.itemmaster.service.base.PostService;
//import cn.com.bbut.iy.itemmaster.service.bm.BmService;
//import cn.com.bbut.iy.itemmaster.util.ExportUtil;
//import cn.shiy.common.baseutil.Container;
//import cn.shiy.common.pmgr.service.PermissionService;
//
///**
// * BM管理
// *
// * @author songxz
// */
//@Controller
//@Slf4j
//@Secure
//@RequestMapping(value = Constants.REQ_HEADER + "/bm")
//// @TimeCheck(from = Constants.TIME_HALT_FROM, to = Constants.TIME_HALT_TO)
//public class BmController extends BaseAction {
//    @Autowired
//    private DptService dptService;
//    @Autowired
//    private StoreService storeService;
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private OccupService occupService;
//    @Autowired
//    private BmService service;
//    @Autowired
//    private IyItemMService itemService;
//
//    /** 编辑画面的类型：0 Add **/
//    private final static int INSER_TYPE = 0;
//    /** 编辑画面的类型：1 修改 **/
//    private final static int UODATE_TYPE = 1;
//    /** 编辑画面的类型：2 审核 **/
//    private final static int CHECK_TYPE = 2;
//    /** 编辑画面的类型：3查看 **/
//    private final static int VIEW_TYPE = 3;
//    /** dpt中第二位 **/
//    private final static String STR_NINE = "9";
//
//    private final String LOG_PAGE_EXPLAIN = "0：Add，1：修改，2：审核，3：查看";
//
//    private final String EXCEL_EXPORT_KEY = "EXCEL_BM";
//    private final String EXCEL_EXPORT_NAME = "BM一览导出.xlsx";
//
//    /**
//     * BM管理画面
//     *
//     * @param request
//     * @param session
//     * @param model
//     * @return
//     */
//    @SuppressWarnings({ "unchecked" })
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_VIEW,
//            PermissionCode.P_CODE_BM_DIV_LEADER_VIEW, PermissionCode.P_CODE_BM_SYS_VIEW,
//            PermissionCode.P_CODE_BM_STORE_VIEW })
//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
//                                   Map<String, ?> model) {
//        User u = this.getUser(session);
//        log.debug("User:{} 进入 BM管理画面", u.getUserId());
//        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
//                Constants.SESSION_ROLES);
//        ModelAndView mv = new ModelAndView("/other_m/bm/bmlist");
//        // 画面是否可用标记，1登录人权限不混乱，2采购或者店铺人员的资源合法
//        Integer use = ConstantsDB.COMMON_ZERO;
//        String useMsg = "";
//        Integer identity = getIdentity(roleIds);
//        if (identity.equals(ConstantsDB.COMMON_ZERO)) {
//            // 1登录人权限混乱
//            use = ConstantsDB.COMMON_ONE;
//            useMsg = "对不起！在BM管理中系统无法识别您身份权限，请联系admin重新修改权限分配后再试。";
//        }
//        if (use == ConstantsDB.COMMON_ZERO && identity.equals(ConstantsDB.COMMON_ONE)) {
//            // 采购
//            BmUserInfoDTO info = service.getStaffDpt(PermissionCode.P_CODE_BM_PRO_VIEW);
//            if (info == null) {
//                use = ConstantsDB.COMMON_ONE;
//                useMsg = "对不起！在BM管理中系统无法识别您的DPT信息，您可能存在跨部门的DPT资源，请联系admin重新分配DPT后再试。";
//            } else {
//                String subDpt = info.getDpt().substring(1, 2);
//                if (subDpt.equals(STR_NINE)) {
//                    use = ConstantsDB.COMMON_ONE;
//                    useMsg = "对不起！在BM管理中系统无法识别您的DPT信息，您可能存在跨部门或全事业部等的DPT资源，请联系admin重新分配DPT后再试。";
//                } else {
//                    info.setUserId(u.getUserId());
//                    mv.addObject("bmUserId", info.getUserId());
//                    mv.addObject("div", info.getDiv());
//                    mv.addObject("divName", info.getDivName());
//                    mv.addObject("dep", info.getDep());
//                    mv.addObject("depName", info.getDepName());
//                }
//            }
//        } else if (use == ConstantsDB.COMMON_ZERO && identity.equals(ConstantsDB.COMMON_FOUR)) {
//            // 店铺
//            BmUserInfoDTO info = service.getStaffStore(PermissionCode.P_CODE_BM_STORE_VIEW);
//            info = new BmUserInfoDTO();
//            info.setUserId("0000001");
//            info.setStore("99999");
//            info.setStoreName("全部");
//            if (info == null) {
//                use = ConstantsDB.COMMON_ONE;
//                useMsg = "对不起！在BM管理中系统无法识别您的店铺信息，您可能存在多个店铺资源，请联系admin重新分配店铺后再试。";
//            } else {
//                info.setUserId(u.getUserId());
//                mv.addObject("bmUserId", info.getUserId());
//                // 如果是店铺
//                mv.addObject("store", info.getStore());
//                mv.addObject("storeName", info.getStoreName());
//            }
//        }
//
//        // 验证当前操作人如果是采购，则该人员的资源不可以跨部门
//        mv.addObject("use", use);
//        mv.addObject("useMsg", useMsg);
//        identity = 1;
//        mv.addObject("identity", identity);
//
//        String pcode = "";
//        if (identity.equals(ConstantsDB.COMMON_ONE)) {
//            pcode = PermissionCode.P_CODE_BM_PRO_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_TWO)) {
//            pcode = PermissionCode.P_CODE_BM_DIV_LEADER_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_THREE)) {
//            pcode = PermissionCode.P_CODE_BM_SYS_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_FOUR)) {
//            pcode = PermissionCode.P_CODE_BM_STORE_VIEW;
//        }
//        // 当前人的身份权限为采购、商品部长、系统部则需要取得当前人的审核权限所属的资源。以便于画面中按钮的判断
//        String checkResources = service.getCheckResources(roleIds, identity);
//        mv.addObject("checkResources", checkResources);
//        // 根据权限 得到事业部
////        List<AutoCompleteDTO> lis = dptService.getDivisionByPrmission(pcode);
////        mv.addObject("division", lis);
//        this.saveToken(request);
//        return mv;
//    }
//
//    /**
//     * EXPORT
//     *
//     * @param session
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Permission(codes = { PermissionCode.P_CODE_BM_EXCEL })
//    @RequestMapping(value = "/excel")
//    public String excel(HttpServletRequest request, HttpSession session, BmParamDTO param) {
//        List<Integer> roleIds = (List<Integer>) session.getAttribute(Constants.SESSION_ROLES);
//        BmExcelParam exParam = new BmExcelParam();
//        exParam.setRoleIds(roleIds);
//        exParam.setPCode(PermissionCode.P_CODE_BM_EXCEL);
//        exParam.setParam(param);
//        exParam.setExFileName(EXCEL_EXPORT_NAME);
//        ExService service = Container.getBean("bmExcelService", ExService.class);
//        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
//    }
//
//    /**
//     * Add画面跳转
//     *
//     * @param req
//     * @param session
//     * @param model
//     * @param identity
//     *            由list页面带入，1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @return
//     */
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_EDIT, PermissionCode.P_CODE_BM_SYS_ADD })
//    @RequestMapping(value = "/edit", method = RequestMethod.GET)
//    public ModelAndView toNewAdd(HttpServletRequest req, HttpSession session, Map<String, ?> model,
//                                 String bmNum, Integer identity) {
//        User u = this.getUser(session);
//        ModelAndView mv = new ModelAndView("/other_m/bm/bmedit");
//        List<Ma1000> list = storeService.getAllStore();
//        mv.addObject("IyStorelist", list);
//        // 设定画面类型
//        if (bmNum != null) {
//            log.debug("User:{} 进入 BM管理-编辑画面，画面类型为{}", u.getUserId(), UODATE_TYPE + " - "
//                    + LOG_PAGE_EXPLAIN);
//            mv.addObject("pageType", UODATE_TYPE);
//
//        } else {
//            log.debug("User:{} 进入 BM管理-编辑画面，画面类型为{}", u.getUserId(), INSER_TYPE + " - "
//                    + LOG_PAGE_EXPLAIN);
//            mv.addObject("pageType", INSER_TYPE);
//        }
//        // 画面是否可编辑，当采购人员的dpt跨了部门，则不可以使用
//        Integer use = ConstantsDB.COMMON_ZERO;
//        if (identity.equals(ConstantsDB.COMMON_ONE)) {
//            // 如果是采购Add
//            BmUserInfoDTO info = service.getStaffDpt(PermissionCode.P_CODE_BM_PRO_EDIT);
//            if (info == null) {
//                use = ConstantsDB.COMMON_ONE;
//            }
//            mv.addObject("userDpt", info.getDptDpt());
//        }
//        mv.addObject("use", use);
////        mv.addObject("userStore", u.getStore());
//        mv.addObject("userId", u.getUserId());
//        mv.addObject("identity", identity);
//        mv.addObject("data", null);
//        mv.addObject("pagename", "编辑");
//        this.saveToken(req);
//        return mv;
//    }
//
//    /**
//     * 修改画面跳转
//     *
//     * @param req
//     * @param session
//     * @param model
//     * @param identity
//     *            由list页面带入，1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @return
//     */
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_EDIT, PermissionCode.P_CODE_BM_SYS_ADD })
//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "/bmupdate", method = RequestMethod.GET)
//    public ModelAndView toUpdate(HttpServletRequest request, HttpSession session,
//                                 Map<String, ?> model, Integer identity) {
//        User u = this.getUser(session);
//        ModelAndView mv = new ModelAndView("/other_m/bm/bmupdate");
//        List<Ma1000> list = storeService.getAllStore();
//        mv.addObject("IyStorelist", list);
//        // 设定画面类型
//        log.debug("User:{} 进入 BM管理-修改画面，画面类型为{}", u.getUserId(), UODATE_TYPE + " - "
//                + LOG_PAGE_EXPLAIN);
//        mv.addObject("pageType", UODATE_TYPE);
//
//        // 画面是否可编辑，当采购人员的dpt跨了部门，则不可以使用
//        Integer use = ConstantsDB.COMMON_ZERO;
//        if (identity.equals(ConstantsDB.COMMON_ONE)) {
//            // 如果是采购
//            BmUserInfoDTO info = service.getStaffDpt(PermissionCode.P_CODE_BM_PRO_EDIT);
//            if (info == null) {
//                use = ConstantsDB.COMMON_ONE;
//            }
//            mv.addObject("userDpt", info.getDptDpt());
//        }
//        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
//                Constants.SESSION_ROLES);
//        // 当前人的身份权限为采购、商品部长、系统部则需要取得当前人的审核权限所属的资源。以便于画面中按钮的判断
//        String myCheckResources = service.getCheckResources(roleIds, identity);
//        mv.addObject("myCheckResources", myCheckResources);
//        mv.addObject("use", use);
//        mv.addObject("userStore", u.getStore());
//        mv.addObject("userId", u.getUserId());
//        mv.addObject("identity", identity);
//        mv.addObject("pagename", "修改");
//        this.saveToken(request);
//        return mv;
//    }
//
//    /**
//     * 查看画面/审核跳转 <br>
//     * 当前画面中包含了 查看和审核的功能
//     *
//     * @param req
//     * @param session
//     * @param model
//     * @param bmCode
//     *            bm编码
//     * @param bmType
//     *            bm类型
//     * @param identity
//     *            由list页面带入，1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_VIEW,
//            PermissionCode.P_CODE_BM_DIV_LEADER_VIEW, PermissionCode.P_CODE_BM_SYS_VIEW,
//            PermissionCode.P_CODE_BM_STORE_VIEW })
//    @RequestMapping(value = "/view", method = RequestMethod.GET)
//    public ModelAndView toView(HttpServletRequest request, HttpSession session,
//                               Map<String, ?> model, String bmCode, String bmType, Integer tabletype,
//                               Integer identity, Integer status) {
//        String userId = this.getUserId(session);
//        log.debug("User:{} 进入 BM管理-编辑画面，画面类型为{}", userId, VIEW_TYPE + " - " + LOG_PAGE_EXPLAIN);
//        ModelAndView mv = new ModelAndView("/other_m/bm/bmview");
//        // 设定画面类型
//        mv.addObject("pageType", VIEW_TYPE);
//        mv.addObject("pagename", "查看");
//        mv.addObject("identity", identity);
//        mv.addObject("status", status);
//        BmViewMainDTO data = service.getBmViewData(bmCode, bmType, tabletype);
//        mv.addObject("data", data);
//
//        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
//                Constants.SESSION_ROLES);
//        // 当前人的身份权限为采购、商品部长、系统部则需要取得当前人的审核权限所属的资源。以便于画面中按钮的判断
//        String myCheckResources = service.getCheckResources(roleIds, identity);
//        mv.addObject("myCheckResources", myCheckResources);
//        this.saveToken(request);
//        return mv;
//    }
//
//    /**
//     * 采购提交Add <br>
//     * 带有系统时间验证
//     *
//     * @param request
//     * @param session
//     * @param identity
//     * @param paramJson
//     * @return
//     */
//    @TimeCheck(from = Constants.TIME_BM_HALT_FROM, to = Constants.TIME_BM_HALT_TO, value = ResultTypeEnum.JSON)
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_EDIT }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/editsubmitpro")
//    public AjaxResultDto editSubmitPro(HttpServletRequest request, HttpSession session,
//                                       Integer identity, String paramJson) {
//        return editSubmit(request, session, identity, paramJson);
//    }
//
//    /**
//     * 系统部提交Add <br>
//     *
//     *
//     * @param request
//     * @param session
//     * @param identity
//     * @param paramJson
//     * @return
//     */
//    @Permission(codes = { PermissionCode.P_CODE_BM_SYS_ADD }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/editsubmitsys")
//    public AjaxResultDto editSubmitSys(HttpServletRequest request, HttpSession session,
//                                       Integer identity, String paramJson) {
//        return editSubmit(request, session, identity, paramJson);
//    }
//
//    /**
//     * bm编辑-采购/系统部提交，Add活修改 <br>
//     * 必须要由editsubmitpro、editsubmitsys 两个方法调用，否则不好使！
//     *
//     * @param request
//     * @param session
//     * @param identity
//     *            提交人身份 1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @param paramJson
//     *            数据json
//     * @return
//     */
//    // @Permission(codes = { PermissionCode.P_CODE_BM_PRO_EDIT,
//    // PermissionCode.P_CODE_BM_SYS_ADD })
//    // @RequestMapping(value = "/editsubmit")
//    @ResponseBody
//    private AjaxResultDto editSubmit(HttpServletRequest request, HttpSession session,
//                                     Integer identity, String paramJson) {
//        User u = this.getUser(session);
//        log.debug("User:{} AddBM 操作人身份={}[1：采购，3：系统部]", u.getUserId(), identity);
//        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
//        if (!ard.isSuccess()) {
//            ard.setToKen(ard.getToKen());
//            return ard;
//        }
//        AjaxResultDto res = service.insertBmData(identity, paramJson, u);
//        res.setToKen(ard.getToKen());
//        return res;
//    }
//
//    /**
//     * 确认和驳回提交（由采购身份操作）
//     *
//     * @param request
//     * @param session
//     * @param bmCode
//     *            bm编码
//     * @param bmType
//     *            bmtype
//     * @param identity
//     *            操作人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @param staffResource
//     *            操作人的数据资源
//     * @param dataResource
//     *            当前数据的资源 item+dpt，...
//     * @param opFlg
//     *            操作类型
//     * @param rejectreason
//     *            理由
//     * @return
//     */
//    @TimeCheck(from = Constants.TIME_BM_HALT_FROM, to = Constants.TIME_BM_HALT_TO, value = ResultTypeEnum.JSON)
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_AFFIRM }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/affirmbmandreject")
//    public AjaxResultDto affirmRejectBm(HttpServletRequest request, HttpSession session,
//                                        String bmCode, String bmType, String staffResource, String dataResource, String opFlg,
//                                        String rejectreason, Integer identity) {
//        User u = this.getUser(session);
//        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
//        if (!ard.isSuccess()) {
//            ard.setToKen(ard.getToKen());
//            return ard;
//        }
//        log.debug("User:{} 确认或驳回 BM 编码=" + bmCode + " 类型=" + bmType + " opFlg=" + opFlg,
//                u.getUserId());
//        AjaxResultDto res = service.updateAffirmBm(bmCode, bmType, staffResource, dataResource,
//                opFlg, rejectreason, identity, u);
//        res.setToKen(ard.getToKen());
//        return res;
//
//    }
//
//    /**
//     * 审核和驳回（事业部长（商品部长）和系统部）
//     *
//     * @param request
//     * @param session
//     * @param bmCode
//     *            单个 bm编码
//     * @param bmType
//     *            单个bm类型
//     * @param bmTypeCode
//     *            组合的bm编码和类型，适用于多选时的审核，与单个bm编码和类型只能有一种存在，例如：01-123,02-010,...
//     * @param staffResource
//     *            操作人的数据资源
//     * @param opFlg
//     *            操作类型
//     *
//     * @param rejectreason
//     *            驳回理由
//     * @param identity
//     *            操作人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @return
//     */
//    @TimeCheck(from = Constants.TIME_BM_HALT_FROM, to = Constants.TIME_BM_HALT_TO, value = ResultTypeEnum.JSON)
//    @Permission(codes = { PermissionCode.P_CODE_BM_DIV_LEADER_CHECK,
//            PermissionCode.P_CODE_BM_SYS_CHECK }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/checkandreject")
//    public AjaxResultDto checkRejectBm(HttpServletRequest request, HttpSession session,
//                                       String bmCode, String bmType, String bmTypeCode, String staffResource, String opFlg,
//                                       String rejectreason, Integer identity) {
//        User u = this.getUser(session);
//        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
//        if (!ard.isSuccess()) {
//            ard.setToKen(ard.getToKen());
//            return ard;
//        }
//        log.debug("User:{} 审核或驳回 BM 编码=" + bmCode + " 类型=" + bmType + " identity=" + identity
//                + " opFlg=" + opFlg, u.getUserId());
//        AjaxResultDto res = service.updateCheckBm(bmCode, bmType, bmTypeCode, opFlg, staffResource,
//                rejectreason, identity, u);
//        res.setToKen(ard.getToKen());
//        return res;
//
//    }
//
//    /**
//     * 得到 默认角色授权grid集合对象
//     *
//     * @param session
//     * @param model
//     * @param v
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @ResponseBody
//    @RequestMapping(value = "/getdata")
//    public GridDataDTO<BmListGridDataDTO> getData(HttpServletRequest request, HttpSession session,
//                                                  Map<String, ?> model, BmParamDTO param) {
//        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
//                Constants.SESSION_ROLES);
//        param.setRoleIds((List<Integer>) roleIds);
//        return service.getData(param);
//    }
//
//    /**
//     * 删除BM
//     *
//     * @param session
//     * @param model
//     * @param bmCode
//     *            bmcode
//     * @param bmType
//     *            bm类型
//     * @param tableType
//     *            数据来源表 0 正是表，1ck表，2历史表
//     * @param identity
//     *            操作人身份1：采购样式，2:事业部部长，3：系统部，4：店铺
//     * @return
//     */
//    @Permission(codes = { PermissionCode.P_CODE_BM_PRO_DEL, PermissionCode.P_CODE_BM_SYS_DEL }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/delbm")
//    public AjaxResultDto delBm(HttpServletRequest request, HttpSession session,
//                               Map<String, ?> model, String bmCode, String bmType, Integer tableType, Integer identity) {
//        User u = this.getUser(session);
//        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
//        if (!ard.isSuccess()) {
//            ard.setToKen(ard.getToKen());
//            return ard;
//        }
//        log.debug("User:{} 删除BM ", u.getUserId());
//        AjaxResultDto res = service.deleteBmByParam(bmCode, bmType, tableType, identity, u);
//        res.setToKen(ard.getToKen());
//        return res;
//
//    }
//
//    @Permission(codes = { PermissionCode.P_CODE_BM_SYS_BATCH_REMOVE }, returnType = ResultTypeEnum.JSON)
//    @ResponseBody
//    @RequestMapping(value = "/dellistbm")
//    public AjaxResultDto delListBm(HttpServletRequest request, HttpSession session,
//                                   Map<String, ?> model, String endDate) {
//        User u = this.getUser(session);
//        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
//        if (!ard.isSuccess()) {
//            ard.setToKen(ard.getToKen());
//            return ard;
//        }
//        log.debug("User:{}  过期BM批量删除 ", u.getUserId());
//        AjaxResultDto res = service.deleteListBmByEndDate(endDate, u);
//        res.setToKen(ard.getToKen());
//        return res;
//
//    }
//
//    /**
//     * 得到 得到部集合
//     *
//     * @param session
//     * @param type
//     *            bm 类型：01、02、.....
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getdepartments")
//    public List<AutoCompleteDTO> getDepartments(HttpSession session, String division,
//                                                Integer identity) {
//        String pcode = "";
//        if (identity.equals(ConstantsDB.COMMON_TWO)) {
//            pcode = PermissionCode.P_CODE_BM_DIV_LEADER_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_THREE)) {
//            pcode = PermissionCode.P_CODE_BM_SYS_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_FOUR)) {
//            pcode = PermissionCode.P_CODE_BM_STORE_VIEW;
//        }
//        List<AutoCompleteDTO> department = dptService.getDepartmentByPrmission(division, pcode);
//        return department;
//    }
//
//    /**
//     * 得到 根据code或名称得到职务集合
//     *
//     * @param session
//     * @param type
//     *            bm 类型：01、02、.....
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getbmcode")
//    public BmCodeDTO getBmCode(HttpSession session, String type) {
//        return service.getBmCodeByType(type);
//    }
//
//    /**
//     * 指定Item BarcodeM表信息
//     *
//     * @param session
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getiteminfobyitem1")
//    public BmItemResultDto getItemInfoByItem1(HttpSession session, String item1) {
//        BmItemResultDto rs = service.getItemInfoByItem1(item1);
//        return rs;
//    }
//
//    /**
//     * 指定Item Barcode和店铺，查询控制记录，c表信息
//     *
//     * @param session
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/isitembmexist")
//    public AjaxResultDto isItemBmExist(HttpSession session, String item1, String stores) {
//        AjaxResultDto rs = service.isItemBmExist(item1, stores);
//        return rs;
//    }
//
//    /**
//     * 验证商品的销售开始日期和结束日，是否在各个店铺的有效期内
//     *
//     * @param session
//     * @param itemSystem
//     * @param stores
//     * @param startDate
//     * @param endDate
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/verdictitemindate")
//    public AjaxResultDto verdictItemIndate(HttpSession session, String itemSystem, String stores,
//                                           String startDate, String endDate) {
//        AjaxResultDto rs = service.verdictItemIndate(itemSystem, stores, startDate, endDate);
//        return rs;
//    }
//
//    /**
//     * 验证商品的销售开始日期和结束日，是否在各个店铺的有效期内 <br>
//     * 根据参数得到 该单品的c表数据，其中由销售日期作为进货/销售单价的取值范围
//     *
//     * @param session
//     * @param itemSystem
//     *            系统码
//     * @param stores
//     *            店铺集合 逗号分隔
//     * @param startDate
//     *            yyyymmdd
//     * @param endDate
//     *            yyyymmdd
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getitemstoreinfo")
//    public AjaxResultBmDto getItemStoreInfo(HttpSession session, String itemSystem, String stores,
//                                            String startDate, String endDate) {
//        AjaxResultBmDto res = service.getItemStoreInfo(itemSystem, stores, startDate, endDate);
//        return res;
//    }
//
//    /**
//     * 验证商品的销售开始日期和结束日，是否在各个店铺的有效期内
//     *
//     * @param session
//     * @param itemSystem
//     *            系统码
//     * @param stores
//     *            店铺集合 逗号分隔
//     * @param startDate
//     *            yyyymmdd
//     * @param endDate
//     *            yyyymmdd
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getiteminfo")
//    public AjaxResultBmDto getItemInfo(HttpSession session, String itemCode) {
//        AjaxResultBmDto res = service.getItemInfoByCode(itemCode);
//        return res;
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getdatabycodetype")
//    public AjaxResultBmDto getDataByCodeType(HttpServletRequest request, HttpSession session,
//                                             Map<String, ?> model, String bmCode, String bmType, Integer identity) {
//        User u = this.getUser(session);
//        AjaxResultBmDto res = service.getDataByCodeType(bmCode, bmType, identity);
//        return res;
//
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/refreshWaitingReview")
//    public AjaxResultBmDto refreshWaitingReview(HttpServletRequest request, HttpSession session,
//                                                Map<String, ?> model, Integer identity) {
//        String pcode = "";
//        if (identity.equals(ConstantsDB.COMMON_TWO)) {
//            pcode = PermissionCode.P_CODE_BM_DIV_LEADER_VIEW;
//        } else if (identity.equals(ConstantsDB.COMMON_THREE)) {
//            pcode = PermissionCode.P_CODE_BM_SYS_VIEW;
//        }
//        AjaxResultBmDto res = new AjaxResultBmDto();
//        Map<String, Integer> map = new HashMap<String, Integer>();
//        // 事业部长 需要审核的数量
//        List<BmListGridDataDTO> checkCount = service.getSecretaryCheckCount(pcode);
//        map.put("checkCount", checkCount.size());
//        // 事业部长 紧急审核的数量
//        Integer urgencyCount = service.getUrgencyCountCount(checkCount);
//        map.put("urgencyCount", urgencyCount);
//        res.setData(map);
//        res.setMessage("数据获取成功");
//        res.setSuccess(true);
//        return res;
//
//    }
//
//    /**
//     * 得到当前操作人的身份类型
//     *
//     * @param roleIds
//     *            当前操作人的角色
//     *
//     * @return 身份 0：身份混乱不可操作；:1：采购样式，2:大事业部(商品部长)部长 3：系统部，4：店铺样式
//     */
//    private Integer getIdentity(Collection<Integer> roleIds) {
//        PermissionService pService = Container.getBean(PermissionService.class);
//        Integer identity = ConstantsDB.COMMON_ZERO;
//        int hasPcodeCount = ConstantsDB.COMMON_ZERO;
//        // 是否有采购查看权限
//        boolean isHas = pService.isRolesHasPermission(roleIds, PermissionCode.P_CODE_BM_PRO_VIEW);
//        if (isHas) {
//            identity = ConstantsDB.COMMON_ONE;
//            hasPcodeCount++;
//        }
//        // 是否有事业部长（商品部长）查看权限
//        isHas = pService.isRolesHasPermission(roleIds, PermissionCode.P_CODE_BM_DIV_LEADER_VIEW);
//        if (isHas) {
//            identity = ConstantsDB.COMMON_TWO;
//            hasPcodeCount++;
//        }
//        // 是否有系统部查看权限
//        isHas = pService.isRolesHasPermission(roleIds, PermissionCode.P_CODE_BM_SYS_VIEW);
//        if (isHas) {
//            identity = ConstantsDB.COMMON_THREE;
//            hasPcodeCount++;
//        }
//        // 是否有店铺查看权限
//        isHas = pService.isRolesHasPermission(roleIds, PermissionCode.P_CODE_BM_STORE_VIEW);
//        if (isHas) {
//            identity = ConstantsDB.COMMON_FOUR;
//            hasPcodeCount++;
//        }
//        if (hasPcodeCount > 1) {
//            return 0;
//        }
//        return identity;
//    }
//}
