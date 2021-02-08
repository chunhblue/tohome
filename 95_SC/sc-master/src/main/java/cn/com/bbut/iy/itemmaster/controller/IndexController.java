package cn.com.bbut.iy.itemmaster.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.task.TodoTaskGridDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.TaskService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;

/**
 * @author songxz
 */
@Controller
@RequestMapping(value = Constants.REQ_HEADER)
@Slf4j
@Secure
public class IndexController extends BaseAction{
    @Autowired
    private TaskService taskService;
    @Autowired
    private CM9060Service cm9060Service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_TODO_TASKS_LIST_VIEW})
    public ModelAndView view(HttpSession session, Map<String, ?> model, String msg) {
        ModelAndView mv = new ModelAndView("index", model);
        mv.addObject("msg", msg);
        return mv;
    }

    /**
     * 获取TodoTask数量
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getTodoTask", method = RequestMethod.GET)
    @ResponseBody
    public GridDataDTO<TodoTaskGridDto> getTodoTask(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        GridDataDTO<TodoTaskGridDto> grid = new GridDataDTO<TodoTaskGridDto>();
        //获取角色集合
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取店铺cd集合
        Collection<String> storeCds = (Collection<String>) request.getSession().getAttribute(
                Constants.SESSION_STORES);
        grid.setRows(taskService.getTodoTask(roleIds,storeCds,u.getUserId()));
        return grid;
    }

    /**
     * 轮询 通知回复提示
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getNotificationsCount", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResultDto getNotificationsCount(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        AjaxResultDto resultDto = new AjaxResultDto();
        //获取角色集合
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取店铺cd集合
        Collection<String> storeCds = (Collection<String>) request.getSession().getAttribute(
                Constants.SESSION_STORES);
        if(storeCds==null||storeCds.size() == 0){
            resultDto.setSuccess(false);
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return resultDto;
        }
        //获取通知数量
        int notificationCount = taskService.getNotificationsCount(roleIds,storeCds,u.getUserId());
        //获取通知数量
        int mmPromotionCount = taskService.getMMPromotionCount(u.getUserId());
        //获取通知数量
        int newItemCount = taskService.getNewItemCount(u.getUserId(),storeCds);
        resultDto.setSuccess(true);
        Map<String,Integer> countMap = new HashMap<>();
        countMap.put("count",notificationCount+mmPromotionCount+newItemCount);
        countMap.put("notificationCount",notificationCount);
        countMap.put("mmPromotionCount",mmPromotionCount);
        countMap.put("newItemCount",newItemCount);
        resultDto.setData(countMap);
        return resultDto;
    }

    /**
     * 验证当前时间是否禁用
     * @param session
     * @param req
     * @return
     */
    @RequestMapping(value = "/batchCheck")
    @ResponseBody
    public AjaxResultDto batchCheck(HttpSession session, HttpServletRequest req) {
        AjaxResultDto resultDto = new AjaxResultDto();
        String nrRunFlag = cm9060Service.getValByKey("0005");
        if("1".equals(nrRunFlag)){
            resultDto.setMessage("Currently unavailable！");//当前正在日结不可操作
            resultDto.setSuccess(false);
        }else{
            resultDto.setSuccess(true);
        }
        return resultDto;
    }
}
