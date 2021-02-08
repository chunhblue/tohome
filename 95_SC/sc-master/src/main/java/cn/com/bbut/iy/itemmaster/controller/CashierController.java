package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050GridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050ParamDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.SA0050Service;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 收银员管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cashier")
public class CashierController extends BaseAction {
    @Autowired
    private SA0050Service sa0050Service;
    @Autowired
    private Ma1000Service ma1000Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;
    private final String EXCEL_EXPORT_KEY = "EXCEL_CASHIER_MANAGETMENT";
    private final String EXCEL_EXPORT_NAME = "Cashier Managerment.xlsx";
    /**
     * 收银员管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_CASHIER_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 收银员管理", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("cashier/cashierList");
        mv.addObject("useMsg", "收银员管理画面");
        return mv;
    }

    /**
     * 获取收银员列表
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCashierList")
    public GridDataDTO<SA0050GridDto> getCashierList(HttpServletRequest request, HttpSession session,
                                                 Map<String, ?> model, SA0050ParamDto paramDto) {

        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, paramDto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<SA0050GridDto>();
        }
        paramDto.setStores(stores);
        GridDataDTO<SA0050GridDto> grid  = sa0050Service.getList(paramDto);
        return grid;
    }
    @RequestMapping(value = "/export")
//    public String export(HttpServletRequest request, HttpSession session, SA0050ParamDto paramDto){
    public String export(HttpServletRequest request, HttpSession session, String searchJson){
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        SA0050ParamDto param = gson.fromJson(searchJson, SA0050ParamDto.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_CASHIER_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("cashierManagerExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
    /**
     * 获取营业员信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCashier")
    public ResultDto getCashier(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String cashierId,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)){
            SA0050 sa0050 = sa0050Service.getCashier(cashierId);
            if(sa0050!=null){
                resultDto.setSuccess(true);
                resultDto.setData(sa0050);
            }
        }else{
            resultDto.setMessage("Cashier ID cannot be empty!");
        }
        return resultDto;
    }
    @ResponseBody
    @RequestMapping(value = "/getCashier1")
    public ResultDto getCashier1(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String cashierId,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)){
            SA0050 sa0050 = sa0050Service.getCashier1(cashierId,storeCd);
            if(sa0050!=null){
                resultDto.setSuccess(true);
                resultDto.setData(sa0050);
            }
        }else{
            resultDto.setMessage("Cashier ID cannot be empty!");
        }
        return resultDto;
    }
    /**
     * 获取收银员密码信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCashierPwd")
    public ResultDto getCashierPwd(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String cashierId,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)){
            String ccashierPwd = sa0050Service.getCashierPwd(cashierId);
            if(StringUtils.isNotBlank(ccashierPwd)){
                resultDto.setSuccess(true);
                resultDto.setData(ccashierPwd);
            }
        }else{
            resultDto.setMessage("Cashier ID cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 修改收银员状态
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateCashierSts")
    @Permission(codes = {
            PermissionCode.CODE_SC_CASHIER_ELIMINATE,
            PermissionCode.CODE_SC_CASHIER_REACTIVATE,
    })
    public ResultDto updateCashierSts(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String cashierId,String storeCd,String effectiveSts) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)&&
            StringUtils.isNotBlank(effectiveSts)){
            SA0050 sa0050 = new SA0050();
            sa0050.setCashierId(cashierId);
            sa0050.setStoreCd(storeCd);
            sa0050.setEffectiveSts(effectiveSts);
            int flg = sa0050Service.updateCashierSts(sa0050);
            sa0050 = null;
            if(flg>0){
                resultDto.setSuccess(true);
                resultDto.setData(sa0050);
            }else{
                resultDto.setMessage("Failed to modify the cashier effective status!");
            }
        }else{
            resultDto.setMessage("Cashier ID or effective status cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 初始化收银员密码
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/initCashierPwd")
    @Permission(codes = {PermissionCode.CODE_SC_CASHIER_INIT_PASSWORD})
    public ResultDto initCashierPwd(HttpServletRequest request, HttpSession session,String cashierId,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)){
            SA0050 sa0050 = new SA0050();
            sa0050.setCashierId(cashierId);
            sa0050.setStoreCd(storeCd);
            int flg = sa0050Service.updateCashierPwd(sa0050,true);
            sa0050 = null;
            if(flg>0){
                resultDto.setSuccess(true);
                resultDto.setData(sa0050);
            }else{
                resultDto.setMessage("Cashier password initialization failed!");
            }
        }else{
            resultDto.setMessage("Cashier ID cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 修改收银员密码
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateCashierPwd")
    @Permission(codes = {
            PermissionCode.CODE_SC_CASHIER_RESET_PASSWORD
    })
    public ResultDto updateCashierPwd(HttpServletRequest request, HttpSession session,
                                      Map<String, ?> model,String storeCd, String cashierId,String newPassword) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)&&
                StringUtils.isNotBlank(newPassword)){
            SA0050 sa0050 = new SA0050();
            sa0050.setCashierId(cashierId);
            sa0050.setStoreCd(storeCd);
            sa0050.setCashierPassword(newPassword);
            int flg = sa0050Service.updateCashierPwd(sa0050,false);
            sa0050 = null;
            if(flg>0){
                resultDto.setSuccess(true);
                resultDto.setData(sa0050);
            }else{
                resultDto.setMessage("Failed to modify the cashier password!");
            }
        }else{
            resultDto.setMessage("Cashier id and new password cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 新增营业员
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add")
    @Permission(codes = {
            PermissionCode.CODE_SC_CASHIER_ADD
    })
    public ResultDto addCashier(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, SA0050 sa0050) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(sa0050!=null&&StringUtils.isNotBlank(sa0050.getStoreCd())&&
                StringUtils.isNotBlank(sa0050.getCashierId())&&
                StringUtils.isNotBlank(sa0050.getCashierName())&&
                StringUtils.isNotBlank(sa0050.getCashierLevel())&&
                StringUtils.isNotBlank(sa0050.getEffectiveSts())) {
            int flag = sa0050Service.insertCashier(sa0050);
            if(flag>0){
                resultDto.setSuccess(true);
            }
        }else{
            resultDto.setMessage("Parameter cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 删除数据
     * @param session
     * @param cashierId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete")
    @Permission(codes = { PermissionCode.CODE_SC_CASHIER_LIST_DELETE })
    public ResultDto delete(HttpServletRequest request, HttpSession session, String cashierId,String storeCd) {
        ResultDto resultDto = new ResultDto();
        if(StringUtils.isBlank(cashierId)){
            resultDto.setMessage("Parameter cannot be empty!");
            return resultDto;
        }
        int re = sa0050Service.delete(cashierId,storeCd);
        if(re == 1){
            resultDto.setSuccess(true);
            resultDto.setMessage("Deleted successfully!");
        }else{
            resultDto.setMessage("Deleted failed!");
        }
        return resultDto;
    }

    /**
     * 修改收银员权限
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update")
//    @Permission(codes = {
//            PermissionCode.CODE_SC_CASHIER_EDIT
//    })
    public ResultDto updateCashier(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String cashierId,String cashierLevel,String cashierEmail,String  cashierName,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(cashierId)&&
                StringUtils.isNotBlank(cashierLevel)){
            SA0050 sa0050 = new SA0050();
            sa0050.setCashierId(cashierId);
            sa0050.setCashierLevel(cashierLevel);
            sa0050.setCashierName(cashierName);
            sa0050.setCashierEmail(cashierEmail);
            sa0050.setStoreCd(storeCd);
            int flag = sa0050Service.updateCashier(sa0050);
            if(flag>0){
                resultDto.setSuccess(true);
            }
        }else{
            resultDto.setMessage("Parameter cannot be empty!");
        }
        return resultDto;
    }

    @RequestMapping(value = "ma1000/getStore")
    @ResponseBody
    public String getAllStore(HttpSession session, HttpServletRequest req){
        List<Ma1000> list = ma1000Service.getList();
         return  new Gson().toJson(list);
    }

    @RequestMapping(value = "/getCashierId")
    @ResponseBody
    public Map getCashierId(HttpSession session, HttpServletRequest req,String cashierId,String storeCd){
        HashMap<String, Boolean> map = new HashMap<>();
//        map.put("valid",sa0050Service.getCashierIdCount(cashierId));
        map.put("valid",sa0050Service.getCashierIdCount1(cashierId,storeCd));
       return  map;

    }

    @ResponseBody
    @RequestMapping(value = "/confirmPassword")
    public Map confirmPassword(HttpServletRequest request, HttpSession session,
                               String oldPassword,String cashierId,String storeCd) {
        Map<String,Boolean> map = new HashMap<>();
        map.put("valid",sa0050Service.getCashierPwd1(cashierId,oldPassword,storeCd));
        return map;
    }
    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, SA0050ParamDto param){
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
}
