package cn.com.bbut.iy.itemmaster.controller.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemark;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.pmgr.entity.Role;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lilw
 */
@Controller
@Secure
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/role")
public class ViewRoleController extends BaseAction {

	@Autowired
	private StoreService storeService;
	@Autowired
	private IyRoleService service;

	/**
	 * 跳转到角色管理页面
	 * 
	 * @param session
	 * @param req
	 * @return
	 */
	@Permission(code = PermissionCode.CODE_ROLE_LIST_VIEW)
	@RequestMapping(method = RequestMethod.GET)
	public String toManage(HttpSession session, HttpServletRequest req) {
		String userid = this.getUserId(session);
        log.debug("user {} in  跳转到角色管理页面 ", userid);
		return "/base/role/rolelist";
	}

	/**
	 * 跳转到角色编辑页面
	 *
	 * @param session
	 * @param req
	 * @return
	 */
	@Permission(code = PermissionCode.CODE_ROLE_EDIT)
	@RequestMapping(value = "/edit")
	public String toEditInit(HttpSession session, HttpServletRequest req, Integer id, Model model) {

		this.saveToken(req);
		String userid = this.getUserId(session);
        log.debug("user {} in  跳转到角色编辑页面 ", userid);
		if (id != null) {
			Role role = service.getRoleById(id);
			model.addAttribute("role", role);
			IymRoleRemark remark = service.getRoleRemarkByRoleId(id);
			model.addAttribute("remark", remark);
			List<ResourceViewDTO> dtos = service.getResourcesByRoleId(id);
			model.addAttribute("dtos", dtos);
			List<RoleStoreDTO> storeDtos = service.getStoresByRoleId(id);
			model.addAttribute("storeDtos", storeDtos);
		}
		// 获取店铺信息
		List<Ma1000> stores = storeService.getAllStore();
		model.addAttribute("stores", stores);
		model.addAttribute("roleId", id);
		return "/base/role/roleedit";
	}

	/**
	 * 跳转到角色查看页面
	 *
	 * @param session
	 * @param req
	 * @return
	 */
	@Permission(code = PermissionCode.CODE_ROLE_VIEW)
	@RequestMapping(value = "/view")
	public String toViewInit(HttpSession session, HttpServletRequest req, Integer id, Model model) {

		this.saveToken(req);
		String userid = this.getUserId(session);
        log.debug("user {} in  跳转到角色查看页面 ", userid);

		Role role = service.getRoleById(id);
		model.addAttribute("role", role);
		IymRoleRemark remark = service.getRoleRemarkByRoleId(id);
		model.addAttribute("remark", remark);
		List<ResourceViewDTO> dtos = service.getResourcesByRoleId(id);
		model.addAttribute("dtos", dtos);
		List<RoleStoreDTO> storeDtos = service.getStoresByRoleId(id);
		model.addAttribute("storeDtos", storeDtos);
		model.addAttribute("roleId", id);
		return "/base/role/roleview";
	}

}
