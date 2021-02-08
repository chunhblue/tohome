package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.operatorLog.OperatorLogService;
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
@RequestMapping(value = Constants.REQ_HEADER + "/operatorLog")
public class OperatorLogController extends BaseAction {

    @Autowired
    private OperatorLogService operatorLogService;

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_OL_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        User user = this.getUser(session);
        log.debug("User:{} 进入 操作日志画面", user.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(Constants.SESSION_ROLES);

        ModelAndView mv = new ModelAndView("operatorLog/operatorLog");
        mv.addObject("useMsg", "操作日志画面");
        return mv;
    }

    /**
     * 查询操作记录
     */
    @RequestMapping("/search")
    @ResponseBody
    public GridDataDTO<OperatorLogDTO> search(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        if (searchJson==null|| StringUtils.isEmpty(searchJson)) {
            return null;
        }
        Gson gson = new Gson();
        OperatorLogParamDTO param = gson.fromJson(searchJson, OperatorLogParamDTO.class);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return operatorLogService.search(param);
    }

}

