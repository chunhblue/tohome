package cn.com.bbut.iy.itemmaster.controller.data;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.*;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemark;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.shiy.common.pmgr.entity.Role;
import cn.shiy.common.pmgr.enums.RoleGrantEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author lilw
 */
@RestController
@Secure
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/role")
public class RoleController extends BaseAction {

	@Autowired
	private IyRoleService service;

	@Permission(code = PermissionCode.CODE_ROLE_LIST_VIEW)
	@RequestMapping(value = "/list")
	public GridDataDTO<RoleGridDataDTO> getDataList(HttpSession session, HttpServletRequest req,
			RoleParamDTO param) {
		String userid = this.getUserId(session);
        log.debug("user {} in  获取角色管理列表数据 ", userid);

		GridDataDTO<RoleGridDataDTO> data = service.getRoleListByParam(param);

		return data;
	}

	@RequestMapping(value = "/loadperm")
	public List<ParentMenuDTO> loadPermissions(HttpSession session, HttpServletRequest req,
			Integer roleId) {
		String userid = this.getUserId(session);
		log.debug("user {} in  加载角色编辑页面中的权限信息 ", userid);
		List<ParentMenuDTO> perms = service.getMenuAndPermForRole(roleId);
		return perms;
	}

	@Permission(code = PermissionCode.CODE_ROLE_EDIT)
	@RequestMapping(value = "/submitData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public AjaxResultDto submitData(HttpSession session, HttpServletRequest req, IyRoleDTO iyRole) {
		String userid = this.getUserId(session);
		AjaxResultDto ard = ajaxRepeatSubmitCheck(req, session);
		if (ard.isRepeat()) {
			return ard;
		}
		log.debug("user {} in  角色信息提交 ", userid);
		Role role = new Role();
		role.setId(iyRole.getId());
		role.setName(iyRole.getName());
		role.setGrant(RoleGrantEnum.ENABLED);
		IymRoleRemark remark = new IymRoleRemark();
		remark.setRemark(StringUtils.isBlank(iyRole.getRemark()) ? " " : iyRole.getRemark());
		remark.setUpdateTime(TimeUtil.getDate());
		remark.setUpdateUserid(userid);
		try {
			/*String _str = iyRole.getStoresStr();
			List<RoleStoreDTO> _list = null;
			if(StringUtils.isNotBlank(_str)){
				Gson gson = new Gson();
				_list = gson.fromJson(_str, new TypeToken<List<RoleStoreDTO>>(){}.getType());
			}
			boolean b = service.addRole(role, remark, iyRole.getResources(),
					iyRole.getPermissionsStr(), iyRole.getMenusStr(), _list);
			*/
			boolean b = service.addRole(role, remark, null,
					iyRole.getPermissionsStr(), iyRole.getMenusStr(), null);
			ard.setSuccess(b);
			if (!b) {
				ard.setMessage("Operation failed!");
			}
		} catch (SystemRuntimeException sre) {
			log.debug("角色编辑失败，user：{}", userid, sre);
			ard.setSuccess(false);
			ard.setMessage(sre.getMessage());
		} catch (Exception e) {
			log.debug("角色编辑异常，user：{}", userid, e);
			ard.setSuccess(false);
			ard.setMessage("Operation failed! Please contact your system administrator!");
		}
		return ard;
	}

	@Permission(code = PermissionCode.CODE_ROLE_DEL)
	@RequestMapping(value = "/deleteData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public AjaxResultDto deleteData(HttpSession session, HttpServletRequest req, Integer roleId) {
        String userid = this.getUserId(session);
        log.debug("user {} in  删除角色信息 ", userid);
        AjaxResultDto ard = new AjaxResultDto();
        try {
            boolean b = service.delRole(roleId);
            ard.setSuccess(b);
        } catch (SystemRuntimeException sre) {
            log.debug("角色删除失败，user：{}", userid, sre);
            ard.setSuccess(false);
            ard.setMessage(sre.getMessage());
        } catch (Exception e) {
            log.debug("角色删除异常，user：{}", userid, e);
            ard.setSuccess(false);
            ard.setMessage("Operation failed! Please contact your system administrator!");
        }
        return ard;
    }

    @RequestMapping(value = "/getrolename", method = RequestMethod.GET)
    public List<AutoCompleteDTO> getRoles(HttpSession session, HttpServletRequest req, String v,
            String userId) {
        String userid = this.getUserId(session);
        log.debug("user {} in  autocomplete获取角色信息 ", userid);
        if (StringUtils.isBlank(userId)) {
            userId = userid;
        }
        List<AutoCompleteDTO> roles = service.getRolesLikeName(userId, v);
        return roles;
    }

}
