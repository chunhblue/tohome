package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.Ma4160Service;
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
 * @ClassName Ma4160Controller
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/3 17:07
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/officeManagement")
public class Ma4160Controller extends  BaseAction{
    @Autowired
    private  Ma4160Service ma4160Service;
    @RequestMapping(method = RequestMethod.GET)

    @Permission(codes = { PermissionCode.CODE_SC_OFF_MANAGE_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        User user = this.getUser(session);
        log.debug("User:{} 进入 职务检索画面", user.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);

        ModelAndView mv = new ModelAndView("storepositiondataLd/storepositionlist");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "职务检索画面");
        return mv;
    }



    @ResponseBody
   @RequestMapping(value = "/getList")
    public GridDataDTO<MA4160DTO> getList(HttpServletRequest request, HttpSession session,
                                         int page, int rows){
        if(this.getUser(session) == null){
            return new GridDataDTO<MA4160DTO>();
        }
        MA4160ParamDTO ma4160Param;
        ma4160Param= new MA4160ParamDTO();

        ma4160Param.setPage(page);
        ma4160Param.setRows(rows);
       // ma4160Param.setJobTypeCd("000006");
        ma4160Param.setLimitStart((page-1)*rows);
       // System.out.println(ma4160Service.selectAll(ma4160Param).toString());

        GridDataDTO<MA4160DTO> ma4160DTOGridDataDTO = ma4160Service.selectAll(ma4160Param);
        System.out.println(ma4160DTOGridDataDTO.toString());

        return  ma4160DTOGridDataDTO;
    }


    @ResponseBody
    @RequestMapping(value = "/searchOne")
    public GridDataDTO<MA4160DTO> search(HttpServletRequest request, HttpSession session,
                                         int page, int rows,String searchJson){
        Gson gson = new Gson();
        MA4160ParamDTO ma4160ParamDTO = gson.fromJson(searchJson, MA4160ParamDTO.class);
        System.out.println(ma4160ParamDTO);
        ma4160ParamDTO.setPage(page);
        ma4160ParamDTO.setRows(rows);
        ma4160ParamDTO.setLimitStart((page-1)*rows);
        GridDataDTO<MA4160DTO> data= ma4160Service.search(ma4160ParamDTO);
        return data;
    }

    /**
     * 判断是否有SM AM权限
     * @param request
     * @param session
     * @param storeCd
     * @return
     */
    @RequestMapping(value = "/checkPosition")
    @ResponseBody
    public AjaxResultDto checkPosition(HttpServletRequest request, HttpSession session,String storeCd) {
        User u = this.getUser(session);
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
//        if (!res.isSuccess()) {
//            res.setToKen(res.getToKen());
//            return res;
//        }
        if(StringUtils.isBlank(storeCd)||
                StringUtils.isBlank(u.getUserId())){
            res.setSuccess(false);
            res.setMessage("Submitted failed,The store or userId cannot be empty!");
            return res;
        }
        int num = ma4160Service.getPositionByStoreCd(storeCd,u.getUserId());
        if(num<=0){
            res.setMessage("You do not have permission to submit it!");
            res.setSuccess(false);
            return res;
        }
        res.setSuccess(true);
        return res;
    }

    @RequestMapping(value = "/checkUserRole")
    @ResponseBody
    public AjaxResultDto checkUserRole(HttpServletRequest request, HttpSession session
            ,String storeCd) {
        User u = this.getUser(session);
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if(StringUtils.isBlank(storeCd)||
                StringUtils.isBlank(u.getUserId())){
            res.setSuccess(false);
            res.setMessage("The store or userId cannot be empty!");
            return res;
        }
        int count = ma4160Service.checkAuditByStoreCdAndUserId(storeCd,u.getUserId());
        if(count==0){
            res.setSuccess(false);
            return res;
        }
        res.setSuccess(true);
        return res;
    }

    @RequestMapping(value = "/getPositionList")
    @ResponseBody
    public AjaxResultDto getPosition(HttpServletRequest request, HttpSession session,String storeCd) {
        User u = this.getUser(session);
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if(StringUtils.isBlank(storeCd)||
                StringUtils.isBlank(u.getUserId())){
            res.setSuccess(false);
            res.setMessage("Submitted failed,The store or userId cannot be empty!");
            return res;
        }
        List<Integer> _list = ma4160Service.getPositionList(storeCd,u.getUserId());
        if(_list.size() ==0 ){
            res.setMessage("You do not have permission!");
            res.setSuccess(false);
            return res;
        }
        res.setData(_list);
        res.setSuccess(true);
        return res;
    }
}
