package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.UserService;
import cn.com.bbut.iy.itemmaster.service.mySubmissions.MySubmissionsService;
import cn.com.bbut.iy.itemmaster.service.mySubmissions.SubbmitterService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/submissions")
public class MySubmissionsController extends BaseAction{

    @Autowired
    private UserService userService;
    @Autowired
    private MySubmissionsService mySubmissionsService;
    @Autowired
    private SubbmitterService subbmitterService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toList(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店提交单据一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("auditMessage/mySubmissions");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "提交单据画面");
        this.saveToken(request);
        return mv;
    }


    @SuppressWarnings("unchecked") //抑制所有类型的警告
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<MySubmissionsGridDTO> togetList(HttpServletRequest request, HttpSession session,
                                                       int page, int rows, String searchJson){
        if(this.getUser(session) == null){
            return new GridDataDTO<>();
        }
        Gson gson = new Gson();
        MySubmissionsParamDTO subParam;
        if(searchJson == null){
            subParam = new MySubmissionsParamDTO();
        }
        subParam = gson.fromJson(searchJson, MySubmissionsParamDTO.class); // 参数转换

        subParam.setPage(page);
        subParam.setRows(rows);
        subParam.setLimitStart(rows * (page-1));
        return mySubmissionsService.getSubmissionsList(subParam);
    }

    /**
     * 跳转到提交编码查询页面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/subbmitter")
    public ModelAndView toSubbmitterQuery(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model){
        User u = this.getUser(session);
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("auditMessage/subbmitterQuery");
        mv.addObject("use", 0);
        mv.addObject("useMsg", "提交编码查询画面");
        return mv;
    }

    @SuppressWarnings("unchecked") //抑制所有类型的警告
    @ResponseBody
    @RequestMapping(value = "/subbmitter/getSubbmitterList")
    public GridDataDTO<MySubbmitterGridDTO> togetSubbmitterList(HttpServletRequest request, HttpSession session,
                                                      int page, int rows, String searchJson){
        if(this.getUser(session) == null){
            return new GridDataDTO<>();
        }
        Gson gson = new Gson();
        MySubbmitterParamDTO subParam;
        if(searchJson == null){
            subParam = new MySubbmitterParamDTO();
        }
        subParam = gson.fromJson(searchJson, MySubbmitterParamDTO.class); // 参数转换

        subParam.setPage(page);
        subParam.setRows(rows);
        subParam.setLimitStart(rows * (page-1));
        return subbmitterService.getSubmitterList(subParam);
    }
}
