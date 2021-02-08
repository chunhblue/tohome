package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.order.OrderFailedService;
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
import java.util.Collection;
import java.util.Map;

@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/dayOrderFailure")
public class OrderFailureDayController extends BaseAction{
    @Autowired
    private OrderFailedService service;
    @Autowired
    private CM9060Service cm9060Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_FAILED_ORDER_DAY";
    private final String EXCEL_EXPORT_NAME = "Failed Order List of the day.xlsx";

    /**
     * 订货失败管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_OD_FAILED_DAY_VIEWLIST})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 订货失败", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("orderFailedOfTheDay/orderFailed");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("date", date);
        mv.addObject("useMsg", "订货失败画面");
        return mv;
    }

    /**
     * 条件查询订货失败订单
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getdata")
    @ResponseBody
    public GridDataDTO<OrderFailedGridDTO> getFailedList(HttpServletRequest request, HttpSession session,
                                                         int page, int rows, String searchJson) {
        Gson gson = new Gson();
        OrderFailedParamDTO param = gson.fromJson(searchJson, OrderFailedParamDTO.class);
        if(param == null){
            param = new OrderFailedParamDTO();
        }

        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);

        return service.dayOrderFailure(param);
    }

    /**
     * 订货失败管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/view")
    @Permission(codes = { PermissionCode.CODE_SC_OD_FAILED_DAY_VIEW})
    public ModelAndView toFailedOrderView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,OrderFailedGridDTO orderFailedGridDTO) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 订货失败", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("orderFailedOfTheDay/orderFailedView");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("dto", orderFailedGridDTO);
        mv.addObject("useMsg", "订货失败明细画面");
        return mv;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/view/getDetaildata")
    @ResponseBody
    public GridDataDTO<OrderFailedGridDTO> getDetaildata(HttpServletRequest request, HttpSession session,
                                                         int page, int rows, String searchJson) {
        Gson gson = new Gson();
        OrderFailedParamDTO param = gson.fromJson(searchJson, OrderFailedParamDTO.class);
        if(param == null){
            param = new OrderFailedParamDTO();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);

        return service.getDetaildata(param);
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_OD_FAILED_DAY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_OD_FAILED_DAY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("dayOrderFailureExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
}
