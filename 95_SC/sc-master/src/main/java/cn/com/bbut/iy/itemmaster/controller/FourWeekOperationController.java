package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmUserInfoDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.com.bbut.iy.itemmaster.dto.TestDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 4周营业查询
 *
 * @author mxy
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/4WeekOperation")
public class FourWeekOperationController extends BaseAction {
    /**
     * 4周营业查询画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    //@Permission(codes = { PermissionCode.SC_CODE_ORDER_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 4周营业查询画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("information/4Week/4Week");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "4周营业查询画面");
        return mv;
    }

    /**
     * 营业速报画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getdata")
    public GridDataDTO<TestDto> getdata(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        List list = new ArrayList<TestDto>();

        GridDataDTO<TestDto> grid = new  GridDataDTO<TestDto>();
        grid.setRows(list);

        return grid;
    }
    /**
     * 获取周次
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getWeeks")
    @ResponseBody
    public String getWeeks(HttpServletRequest request, HttpSession session, Long date) {
        Date _date = new Date(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(_date);
        int num = calendar.get(Calendar.WEEK_OF_YEAR);
        return ""+num;
    }

}
