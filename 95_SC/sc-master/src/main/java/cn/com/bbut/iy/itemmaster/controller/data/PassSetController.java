package cn.com.bbut.iy.itemmaster.controller.data;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * 密码修改
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/passwordSet")
public class PassSetController extends BaseAction {
    /**
     * 操作员管理一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    //@Permission(codes = { PermissionCode.SC_CODE_ORDER_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView passSet(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入密码修改画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("/base/auth/passSet");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "密码修改画面");
        return mv;
    }
}
