package cn.com.bbut.iy.itemmaster.controller.data;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.com.bbut.iy.itemmaster.service.base.DepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@Secure
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER)
public class DptController extends BaseAction {

	@Autowired
	@Setter
	private DepService service;

    /**
     * 检索事业部信息（部门）
     * 
     * @param session
     * @param req
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/deps")
    public List<DptResourceDTO> getDeps(HttpSession session, HttpServletRequest req, Integer flg,
            String v) {
        User user = this.getUser(session);
        log.debug("抓取部信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = service.getDpts(null,null,null, ConstantsDB.COMMON_THREE, v, v,
                ConstantsDB.COMMON_ONE);
        return dtos;
    }

    /**
     * 检索部信息（大分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/pmas")
    public List<DptResourceDTO> getPmas(HttpSession session, HttpServletRequest req,
            String depId, Integer flg, String v) {
        User user = this.getUser(session);
        log.debug("抓取大分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = service.getDpts(depId,null,null, ConstantsDB.COMMON_TWO, v, v,
                ConstantsDB.COMMON_ONE);
        return dtos;
    }

    /**
     * 检索dpt信息（中分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/categorys")
    public List<DptResourceDTO> getCategorys(HttpSession session, HttpServletRequest req,
            String depId, String pmaId,Integer flg, String v) {
        User user = this.getUser(session);
        log.debug("抓取中分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = service.getDpts(depId,pmaId,null, ConstantsDB.COMMON_ONE, v, v,
                ConstantsDB.COMMON_ONE);
        return dtos;
    }

    /**
     * 检索dpt信息（小分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/subCategorys")
    public List<DptResourceDTO> getSubCategorys(HttpSession session, HttpServletRequest req,
            String depId, String pmaId,String categoryId, Integer flg, String v, String pCode) {
        User user = this.getUser(session);
        log.debug("抓取小分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = service.getDpts(depId,pmaId,categoryId, ConstantsDB.COMMON_ZERO, v, v,
                ConstantsDB.COMMON_ONE);
        return dtos;
    }
}
