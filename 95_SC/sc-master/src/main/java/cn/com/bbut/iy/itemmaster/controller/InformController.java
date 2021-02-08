package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.config.ContextPathConfig;
import cn.com.bbut.iy.itemmaster.config.ExcelConfig;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.*;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma4300.MA4300;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4300Service;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4311Service;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通报管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/informHQ")
public class InformController extends BaseAction {
    @Autowired
    private Ma4300Service ma4300Service;
    @Autowired
    private Ma4311Service ma4311Service;

    /**
     * 通报管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_INF_HQ_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 通报管理", u.getUserId());
        ModelAndView mv = new ModelAndView("inform/informList");
        mv.addObject("useMsg", "通报管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 通报管理头档一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<Ma4300DetailGridDto> getList(HttpServletRequest request, HttpSession session,
                                                    Ma4300DetailParamDto param) {
        return ma4300Service.getList(param);
    }

    /**
     * 通报管理画面新增修改
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_INF_HQ_VIEW,
            PermissionCode.CODE_SC_INF_HQ_ADD
    })
    public ModelAndView orderDetailsAdd(HttpServletRequest request, HttpSession session,
                                        Map<String, ?> model,String operateFlg,String informCd) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 通报管理新增修改画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("inform/informEdit");
        mv.addObject("operateFlg", operateFlg);
        List<RoleStoreDTO> storeDtos = null;
        if(StringUtils.isNotBlank(informCd)){
            storeDtos = ma4311Service.selectListByInformCd(informCd);
            if(storeDtos!=null && !storeDtos.isEmpty()){
                mv.addObject("storeDtos", storeDtos);
            }
        }
        mv.addObject("informCd", informCd);
        mv.addObject("useMsg", "通报管理新增修改管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取角色下拉
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getRoleListAll")
    @ResponseBody
    public List<AutoCompleteDTO> getRoleListAll(HttpSession session, HttpServletRequest req,
                                             String v) {
        User user = this.getUser(session);
        log.debug("抓取角色信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = ma4300Service.getRoleListAll(v);
        return dtos;
    }

    /**
     * 通报信息保存
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_INF_HQ_ADD
    })
    public AjaxResultDto save(HttpServletRequest request, HttpSession session,MA4300 param, String operateFlg) {
        User user = this.getUser(session);
        log.debug("保存通报消息，user:{}", user.getUserId());
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if(param==null||StringUtils.isBlank(operateFlg)||
                StringUtils.isBlank(param.getInformTitle())||
                StringUtils.isBlank(param.getInformStartDate())||
                StringUtils.isBlank(param.getInformEndDate())){
            ard.setSuccess(false);
            ard.setMessage("Save failed! Parameter cannot be empty!");
            return ard;
        }
        boolean result = false;
        param.setUpdateUserId(user.getUserId());
        param.setUpdateYmd(ymd);
        param.setUpdateHms(hms);
        if("add".equals(operateFlg)){
            param.setCreateUserId(user.getUserId());
            param.setCreateYmd(ymd);
            param.setCreateHms(hms);
            String informCd = ma4300Service.insertInform(session,request,param);
            if(StringUtil.isNotBlank(informCd)){
                param.setInformCd(informCd);
                result = true;
            }
        }else if("edit".equals(operateFlg)){
            if(StringUtil.isNotBlank(param.getInformCd())){
                result = ma4300Service.updateInform(param);
            }else{
                ard.setMessage("Save failed! Notification number cannot be empty!");
            }
        }
        ard.setSuccess(result);
        ard.setData(param);
        return ard;
    }

    /**
     * 通报管理头档信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit/getMa4300")
    public ResultDto getMa4300(HttpSession session, String informCd) {
        ResultDto res = new ResultDto();
        res.setSuccess(false);
        if(StringUtils.isNotBlank(informCd)){
            MA4300 dto = ma4300Service.getMa4300(informCd);
            if (dto!=null){
                res.setData(dto);
                res.setSuccess(true);
            }else{
                res.setMessage("Get information is empty！");
            }
        }else{
            res.setMessage("Notification No. cannot be empty!");
        }
        return res;
    }

    /**
     * 通报管理门店范围一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit/getMa4310List")
    @ResponseBody
    public GridDataDTO<Ma4310DetailGridDto> getMa4310List(HttpServletRequest request, HttpSession session,
                                                          String informCd) {
        GridDataDTO<Ma4310DetailGridDto> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(informCd))
            grid.setRows(ma4300Service.getMa4310List(informCd));
        return grid;
    }

    /**
     * 通报管理角色范围一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit/getMa4305List")
    @ResponseBody
    public GridDataDTO<Ma4305DetailGridDto> getMa4305List(HttpServletRequest request, HttpSession session,
                                                          String informCd) {
        GridDataDTO<Ma4305DetailGridDto> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(informCd))
            grid.setRows(ma4300Service.getMa4305List(informCd));
        return grid;
    }

}
