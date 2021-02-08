package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.opreationmanagement.CustOfEntryPlanService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 盘点计划
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/custPlanEntry",produces ={"application/json;charset=UTF-8"})
public class CustPlanEntryController extends BaseAction {

    @Autowired
    private CustOfEntryPlanService custOfEntryPlanService;
    @Autowired
    private MRoleStoreService mRoleStoreService;


    /**
     * 盘点计划一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakePlanList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点计划一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("operationmanplan/custPlanEntry");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_FS_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_FS_STOCK);
        mv.addObject("useMsg", "盘点计划一览画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 盘点计划编辑画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView stocktakePlanEdit(String identity,String enterFlag,String piCdParam, String piDateParam,HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点计划编辑画面", u.getUserId());
        ModelAndView mv = new ModelAndView("operationmanplan/custPlanEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", identity);
        mv.addObject("enterFlag",enterFlag); // 操作状态
        mv.addObject("piCdParam",piCdParam);
        mv.addObject("piDateParam",piDateParam);
        mv.addObject("typeId", ConstantsAudit.TYPE_FS_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_FS_STOCK);
        mv.addObject("useMsg", "盘点计划编辑画面");
        return mv;
    }

    @GetMapping("/getComboxData")
    @ResponseBody
    public ReturnDTO getComboxData(HttpServletRequest request, HttpSession session) {
        Map<String,List<Map<String,String>>> result =  custOfEntryPlanService.getComboxData();
        return new ReturnDTO(true,"ok",result);
    }

    @GetMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String piCd,String storeCd, HttpServletRequest request, HttpSession session) {
        PI0100DTOC pi0110c =  custOfEntryPlanService.getData(piCd,storeCd);
        if (pi0110c==null) {
            // 查询失败
            return new ReturnDTO(false,"error");
        }
        return new ReturnDTO(true,"ok",pi0110c);
    }


    @GetMapping("/getPmaList")
    @ResponseBody
    public ReturnDTO getPmaList(HttpServletRequest request, HttpSession session) {
        List<Map<String, String>> pmaList =  custOfEntryPlanService.getPmaList(request, session);
        return new ReturnDTO(true,"ok",pmaList);
    }

    @Permission(codes = { PermissionCode.CODE_SC_COST_ENTRY_LIST_VIEW})
    @GetMapping("/inquire")
    @ResponseBody
    public GridDataDTO<PI0100DTOC> inquire(String searchJson, int page, int rows,
                                           HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        PI0100ParamDTOC pi0100Paramc = gson.fromJson(searchJson, PI0100ParamDTOC.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100Paramc);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<PI0100DTOC>();
        }
        pi0100Paramc.setStores(stores);
        pi0100Paramc.setPage(page);
        pi0100Paramc.setRows(rows);
        pi0100Paramc.setLimitStart((page - 1)*rows);
        GridDataDTO<PI0100DTOC> data = custOfEntryPlanService.search(pi0100Paramc);
        return data;
    }

    @PostMapping("/save")
    @ResponseBody
    public ReturnDTO save(String record, HttpServletRequest request, HttpSession session) {

        User loginUser = this.getUser(session);

        if (loginUser==null) {
            // 没有登陆
            return new ReturnDTO(false,"请先登录!");
        }

        if (StringUtils.isEmpty(record)) {
            return new ReturnDTO(false,"Parameter cannot be empty!");
        }

        String _record= null;
        try {
            // _record = URLEncoder.encode(URLEncoder.encode(record,"UTF-8"),"UTF-8");
            _record=URLDecoder.decode(record,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (_record==null) {
            return new ReturnDTO(false,"Parameter exception!");
        }

        Gson gson = new Gson();
        PI0100DTOC pi0100c = gson.fromJson(_record, PI0100DTOC.class);
        if (pi0100c==null) {
            return new ReturnDTO(false,"Parameter exception!");
        }
        if (pi0100c.getDetails().size()<1) {
            return new ReturnDTO(false,"明细数据不能为空!");
        }

        if (StringUtils.isEmpty(pi0100c.getFlag()) ||
                (!"add".equals(pi0100c.getFlag()) &&
             !"update".equals(pi0100c.getFlag()))) {
            return new ReturnDTO(false,"操作状态错误!");
        }
        String flag = pi0100c.getFlag();

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dateStr = sdf.format(now);
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];

        // 明细数据
        List<PI0110DTOC> pi0110List = pi0100c.getDetails();

        int row = -1;
        if ("add".equals(flag)) {
            // 新增
            pi0100c.setCreateUserId(loginUser.getUserId());
            pi0100c.setCreateYmd(ymd);
            pi0100c.setCreateHms(hms);
            row = custOfEntryPlanService.insert(pi0100c, pi0110List);
        } else {
            // 修改
            pi0100c.setUpdateUserId(loginUser.getUserId());
            pi0100c.setUpdateYmd(ymd);
            pi0100c.setUpdateHms(hms);
            row = custOfEntryPlanService.update(pi0100c, pi0110List);
        }

        if (row==-1) {
            return new ReturnDTO(false,"保存盘点计划失败!");
        }
        return new ReturnDTO(true,"保存盘点计划成功");
    }

    /**
     * 校验id是否重复
     * @param piCd
     * @param request
     * @param session
     * @return
     */
    @PostMapping("/checkPicd")
    @ResponseBody
    public ReturnDTO checkPicd(String piCd, HttpServletRequest request, HttpSession session) {
        int count = custOfEntryPlanService.checkPicd(piCd);
        if (count<1) {
            // 可以使用, 没有重复
            return new ReturnDTO(true,"ok",count);
        } else {
            // 重复
            return new ReturnDTO(false,"no",count);
        }
    }

    private HSSFWorkbook exl;

    @RequestMapping("/queryExport")
    @ResponseBody
    public ReturnDTO Export(HttpServletRequest request, HttpServletResponse response,HttpSession session, String jsonStr, String excleFormCondition,String piCd,String piDate) {
        ReturnDTO _return = null;
        //获取用户权限
        // 获取session中的操作用户ID
//        SessionUserDTO _sessionUser = (SessionUserDTO) request.getSession().getAttribute("Session_User_Role");
        User _sessionUser = this.getUser(session);
        if(_sessionUser == null){
            // logger.error("登录失效,请重新登陆");
            _return = new ReturnDTO(false, "Session time out!","001");
            return _return;
        }

        // List<String> formulaIdList = JSON.parseObject(ids, List.class);

        // String regionCd=_sessionUser.get();
        // String nOperatorid=_sessionUser.getUserId();
        if(jsonStr == null || jsonStr =="") {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }
        //前端查询条件
        Gson gson = new Gson();
        PI0100ParamDTOC pi0100Paramc = gson.fromJson(jsonStr, PI0100ParamDTOC.class);
        //预留分页
        // PageBounds _page = PageUtil.GetPageBounds(1, 1000, true);

        //返回促销商品的HSSFWorkbook对象
        exl = custOfEntryPlanService.getExportHSSFWorkbook(pi0100Paramc,excleFormCondition,piCd,piDate);
        if(exl == null) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }

        _return = new ReturnDTO(true, "Succeeded!",1);
        return _return;
    }

    /**
     * 导出excle
     */
    @RequestMapping("/download")
    public void export(HttpServletRequest request,HttpServletResponse response) {
        try {
            //下载
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=Stocktake Item.xls");
            OutputStream outputStream = response.getOutputStream();
            exl.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,PI0100ParamDTOC param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
            && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(!StringUtils.isEmpty(param.getStoreCd())){
            stores.add(param.getStoreCd());
            return stores;
        }
        // 只选择了一部分参数，生成查询参数，后台查询
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

}
