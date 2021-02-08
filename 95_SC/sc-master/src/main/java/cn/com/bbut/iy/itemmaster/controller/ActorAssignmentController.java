package cn.com.bbut.iy.itemmaster.controller;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.SubmitDataDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentInfo;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.UserService;
import cn.com.bbut.iy.itemmaster.service.base.IymActorAssignmentService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.shiy.common.pmgr.entity.ActorAssignment;
import cn.shiy.common.pmgr.service.PermissionService;
import lombok.extern.slf4j.Slf4j;

/**
 * 特殊授权
 * 
 * @author lilw
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ua")
public class ActorAssignmentController extends BaseAction {
    @Autowired
    private IymActorAssignmentService service;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permService;

    @Permission(code = PermissionCode.P_CODE_UA_LIST_VIEW)
    @RequestMapping(method = RequestMethod.GET)
    public String view(HttpServletRequest req, HttpSession session, Model model) {
        String userId = this.getUserId(session);
        log.debug("User:{} 进入 特殊授权画面", userId);
        this.saveToken(req);
        boolean b = permService.isActorHasPermission(userId, PermissionCode.P_CODE_OTHER_SYSTEM,
                PermissionCode.P_CODE_OTHER_SELF);
        model.addAttribute("isDS", b ? ConstantsDB.COMMON_ONE : ConstantsDB.COMMON_TWO);
        boolean boo = permService.isActorHasPermission(userId, PermissionCode.P_CODE_OTHER_SYSTEM);
        model.addAttribute("isSysDS", boo ? ConstantsDB.COMMON_ONE : ConstantsDB.COMMON_TWO);
        return "/base/actorassignment/actorassignment";
    }

    @Permission(code = PermissionCode.P_CODE_UA_LIST_VIEW)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody GridDataDTO<ActorAssGridDataDTO> getGridList(HttpServletRequest req,
            HttpSession session, Model model, ActorAssGridParamDTO param) {
        String userId = this.getUserId(session);
        log.debug("User:{} 加载特殊授权列表数据", userId);
        GridDataDTO<ActorAssGridDataDTO> data = service.getGridList(param);
        return data;
    }

    @Permission(code = PermissionCode.P_CODE_UA_CANCEL)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AjaxResultDto delActorAssignment(HttpServletRequest req,
            HttpSession session, Model model, Integer aaId) {
        String userId = this.getUserId(session);
        log.debug("User:{} 删除特殊授权信息", userId);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(req, session);
        if (ard.isRepeat()) {
            return ard;
        }
        try {
            boolean b = service.delActorAssAndInfo(aaId);
            ard.setSuccess(b);
        } catch (SystemRuntimeException sre) {
            log.debug("删除特殊授权失败, user:{}", userId, sre);
            ard.setSuccess(false);
            ard.setMessage(sre.getMessage());
        } catch (Exception e) {
            log.debug("删除特殊授权异常, user:{}", userId, e);
            ard.setSuccess(false);
            ard.setMessage("Operation Error!");
        }

        return ard;
    }

    @RequestMapping(value = "/u", method = RequestMethod.GET)
    public @ResponseBody List<AutoCompleteDTO> getGridList(HttpServletRequest req,
            HttpSession session, Model model, String v) {
        String userId = this.getUserId(session);
        log.debug("User:{} autocomplete检索用户信息", userId);
        List<AutoCompleteDTO> data = userService.getUsersLikeUserId(v);
        return data;
    }

    @Permission(code = PermissionCode.P_CODE_UA_EDIT)
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AjaxResultDto editAA(HttpServletRequest req, HttpSession session,
            Model model, SubmitDataDTO data) {
        String userId = this.getUserId(session);
        log.debug("User:{} 特殊授权编辑提交", userId);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(req, session);
        if (ard.isRepeat()) {
            return ard;
        }

        ActorAssignment aa = new ActorAssignment(data.getId(), data.getUserId(), data.getRoleId());
        IymActorAssignmentInfo info = null;
        if (data.getAssType() == ConstantsDB.COMMON_ONE) {
            Date startDate = TimeUtil.transferFromStringToDate(data.getStartDate(),
                    DateTimeFormatter.ISO_LOCAL_DATE);
            Date endDate = TimeUtil.transferFromStringToDate(data.getEndDate(),
                    DateTimeFormatter.ISO_LOCAL_DATE);
            String userid = userId;
            if (StringUtils.isNotBlank(data.getAssUserId())) {
                userid = data.getAssUserId();
            }
            info = new IymActorAssignmentInfo(null, ConstantsDB.COMMON_ONE, startDate, endDate,
                    TimeUtil.getDate(), userId, userid, ConstantsDB.COMMON_ZERO);
            boolean infob = service.isNotExistActorAssignmentInfo(aa.getId(), info.getAssUserId(),
                    aa.getRoleId());
            if (!infob) {
                ard.setSuccess(false);
                ard.setMessage("Selected privilege has been authorized to others and cannot assign to others again! ");
                return ard;
            }
        }
        boolean boo = service.isNotExistActorAssignment(aa.getActorId(), aa.getRoleId());
        if (!boo) {
            ard.setSuccess(false);
            ard.setMessage("Current user already has selected privilege and cannot be assigned again!");
            return ard;
        }
        try {
            synchronized (this) {
                boolean b = service.updateActorAssignment(aa, info);
                ard.setSuccess(b);
            }
        } catch (SystemRuntimeException sre) {
            log.debug("编辑特殊授权失败, user:{}", userId, sre);
            ard.setSuccess(false);
            ard.setMessage(sre.getMessage());
        } catch (Exception e) {
            log.debug("编辑特殊授权异常, user:{}", userId, e);
            ard.setSuccess(false);
            ard.setMessage("Operation Error!");
        }

        return ard;
    }

}
