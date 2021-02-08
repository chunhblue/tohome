package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070Service;
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

/**
 * 盘点盈亏表
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stocktakePLQ")
public class StockProfitLossController extends BaseAction {

    @Autowired
    private Ma0070Service ma0070Service;

    /**
     * 盘点盈亏表一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    //@Permission(codes = { PermissionCode.SC_CODE_ORDER_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakePlanList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点盈亏表一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stockprofitloss_query/stockPLQuery");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "盘点盈亏表一览画面");
        return mv;
    }

    //      一览页面条件查询
    @ResponseBody
    @RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
    public String query(HttpServletRequest request, HttpSession session,
                        Map<String, ?> model, StocktakeQueryParamDTO param) {
//        return new Gson().toJson(stockPLQueryService.getStocktakeQueryList(param));
        return null;
    }

    /**
     * 请求部门
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDep", method = RequestMethod.GET)
    public String getTDep(HttpServletRequest request, HttpSession session,
                          Map<String, ?> model) {
        return new Gson().toJson(ma0070Service.getList());
    }
}
