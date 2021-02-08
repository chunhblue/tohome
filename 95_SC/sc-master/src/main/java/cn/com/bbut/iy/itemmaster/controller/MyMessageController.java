package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageGridDTO;
import cn.com.bbut.iy.itemmaster.dto.myMessage.MyMessageParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.UserService;
import cn.com.bbut.iy.itemmaster.service.mySubmissions.SubbmitterService;
import cn.com.bbut.iy.itemmaster.service.my_message.MyMessageService;
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
@RequestMapping(value = Constants.REQ_HEADER + "/myMessage")
public class MyMessageController extends BaseAction{

    @Autowired
    private UserService userService;
    @Autowired
    private MyMessageService myMessageService;
    @Autowired
    private SubbmitterService subbmitterService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toList(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店订收货一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("my_message/myMessage");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "我的消息画面");
        return mv;
    }

    @SuppressWarnings("unchecked") //抑制所有类型的警告
    @ResponseBody
    @RequestMapping(value = "/getMessageList")
    public GridDataDTO<MyMessageGridDTO> togetList(HttpServletRequest request, HttpSession session,
                                                   int page, int rows, String searchJson){
        if(this.getUser(session) == null){
            return new GridDataDTO<>();
        }
        Gson gson = new Gson();
        MyMessageParamDTO messParam;
        if(searchJson == null){
            messParam = new MyMessageParamDTO();
        }
        messParam = gson.fromJson(searchJson, MyMessageParamDTO.class); // 参数转换

        messParam.setPage(page);
        messParam.setRows(rows);
        messParam.setLimitStart(rows * (page-1));
        return myMessageService.getMessageList(messParam);
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
}
