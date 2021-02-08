package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070ByPmService;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * 商品分类
 *
 * @author zcz
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma0070")
public class Ma0070Controller extends BaseAction {
    @Autowired
    private Ma0070ByPmService serviceByPm;
    @Autowired
    private Ma0070Service service;

    @RequestMapping(value = "/getDpt")
    public List<MA0070> getDpt(HttpSession session, HttpServletRequest req) {
        return serviceByPm.getListByPm(null);
    }

    @RequestMapping(value = "/getPma")
    public List<MA0080> getPma(HttpSession session, HttpServletRequest req , MA0080 ma0080) {
        return serviceByPm.getListByPm(ma0080,null);
    }

    @RequestMapping(value = "/getCategory")
    public List<MA0090> getCategory(HttpSession session, HttpServletRequest req , MA0090 ma0090) {
        return serviceByPm.getListByPm(ma0090,null);
    }

    @RequestMapping(value = "/getSubCategory")
    public List<MA0100> getSubCategory(HttpSession session, HttpServletRequest req , MA0100 ma0100) {
        return serviceByPm.getListByPm(ma0100,null);
    }
    // 自动填充部门
    @RequestMapping(value = "/getDaprtMent")
    public  List<AutoCompleteDTO> getDaprtMent(HttpSession session,HttpServletRequest request, HttpServletRequest req,
                                               String v) {
        User user = this.getUser(session);
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        log.debug("抓取部门信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        dtos = service.getListByDepart(v);
        return dtos;
    }

}
