package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200GridDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4200;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.ma4200.Ma4200Service;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MA4200Controller
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/10 18:22
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/employesInformation")
public class MA4200Controller extends BaseAction {
    @Autowired
    private Ma4200Service ma4200Service;

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_EM_INFOR_LIST_VIEW })
    public ModelAndView maItemSaleList(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入用户管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storepositiondataLd/storepositionmaintain");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "用户管理画面");
        this.saveToken(request);
        return mv;

    }

    @ResponseBody
    @RequestMapping(value = "/search")
    public GridDataDTO<MA4200GridDTO> getEmpBasicInform(HttpServletRequest request, HttpSession session,
                         MA4200ParamDTO param) {
        if (param==null) {
            param = new MA4200ParamDTO();
        }
        GridDataDTO<MA4200GridDTO> dataDTO = ma4200Service.search(param);
        return dataDTO;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserId")
    public Map getUserId(HttpServletRequest request, HttpSession session,
                                                        String empNumId) {
        Map<String,Boolean> map = new HashMap<>();
        map.put("valid", ma4200Service.getUserIdCount(empNumId.trim()));
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/confirmPassword")
    public Map confirmPassword(HttpServletRequest request, HttpSession session,
                         String oldPassword,String userId) {
        Map<String,Boolean> map = new HashMap<>();
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(oldPassword)){
            map.put("valid",false);
            return map;
        }
        map.put("valid", ma4200Service.confirmPassword(userId,oldPassword));
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_EM_INFOR_ADD ,
            PermissionCode.CODE_SC_EM_INFOR_EDIT
    })
    public AjaxResultDto save(HttpServletRequest request, HttpSession session, MA4200 param,String operateFlg) {
        User loginUser = this.getUser(session);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        String tempToKen = ard.getToKen();
        if(param!=null&&StringUtils.isNotBlank(param.getEmpNumId())
            &&StringUtils.isNotBlank(param.getEmpName())
//            &&StringUtils.isNotBlank(param.getEmpPassword())
            &&StringUtils.isNotBlank(param.getJobTypeCd())
                &&StringUtils.isNotBlank(operateFlg)
            &&StringUtils.isNotBlank(param.getEmpGender())){
            param.setCreateUserId(loginUser.getUserId());
            param.setUpdateUserId(loginUser.getUserId());
            ard = ma4200Service.insertOrUpdate(param,operateFlg);
            ard.setToKen(tempToKen);
        }else{
            ard.setSuccess(false);
            ard.setMessage("Parameter Error!");
        }
        return ard;
    }

    @ResponseBody
    @RequestMapping(value = "/updatePassword")
    @Permission(codes = {PermissionCode.CODE_SC_EM_INFOR_RESET })
    public AjaxResultDto updatePassword(HttpServletRequest request, HttpSession session,
                        String empNumId,String empOldPassword,String empNewPassword) {
        User loginUser = this.getUser(session);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        String tempToKen = ard.getToKen();
        if(StringUtils.isNotBlank(empNumId)
                &&StringUtils.isNotBlank(empOldPassword)
                &&StringUtils.isNotBlank(empNewPassword)){
            MA4200 ma4200 = new MA4200();
            ma4200.setUpdateUserId(loginUser.getUserId());
            ma4200.setEmpNumId(empNumId);
            ma4200.setEmpOldPassword(empOldPassword);
            ma4200.setEmpNewPassword(empNewPassword);
            ard = ma4200Service.updatePassword(ma4200);
            ard.setToKen(tempToKen);
        }else{
            ard.setSuccess(false);
            ard.setMessage("Parameter Error!");
        }
        return ard;
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    @Permission(codes = {PermissionCode.CODE_SC_EM_INFOR_DEL })
    public AjaxResultDto delete(HttpServletRequest request, HttpSession session, String empNumId) {
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        String tempToKen = ard.getToKen();
        if(StringUtils.isNotBlank(empNumId)){
            ard = ma4200Service.delete(empNumId);
            ard.setToKen(tempToKen);
        }else{
            ard.setSuccess(false);
            ard.setMessage("Parameter Error!");
        }
        return ard;
    }

    @ResponseBody
    @RequestMapping(value = "/getStoreInformation")
    public GridDataDTO<MA4200GridDTO> getEmpStoreInfo(int page,int rows,String userCode) {

        GridDataDTO<MA4200GridDTO> dataDTO = ma4200Service.getStore(userCode,page,rows);
        return dataDTO;
    }
}

