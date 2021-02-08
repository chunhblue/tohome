package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 店铺信息
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma1000")
public class Ma1000Controller  extends BaseAction {
    @Autowired
    private Ma1000Service ma1000Service;

    /**
     * 获取店铺下拉  根据用户权限获取
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getStoreByPM")
    @ResponseBody
    public List<AutoCompleteDTO> getStoreByPM(HttpSession session,HttpServletRequest request, HttpServletRequest req,
                                              String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = ma1000Service.getListByStorePm(stores,v);
        }
        return dtos;
    }

    /**
     * 获取店铺区域经理(Area Manager)  根据用户权限获取
     */
    @RequestMapping(value = "/getAMByPM")
    @ResponseBody
    public List<AutoCompleteDTO> getAMByPM(HttpSession session,HttpServletRequest request, HttpServletRequest req,
                                              String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = ma1000Service.getAMByPM(stores,v);
        }
        return dtos;
    }

    @RequestMapping(value = "/getOM")
    @ResponseBody
    public List<AutoCompleteDTO> getOm(HttpSession session,HttpServletRequest request, HttpServletRequest req,
                                           String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = ma1000Service.getOm(stores,v);
        }
        return dtos;
    }
    @RequestMapping(value = "/getOC")
    @ResponseBody
    public List<AutoCompleteDTO> getOc(HttpSession session,HttpServletRequest request, HttpServletRequest req,
                                       String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = ma1000Service.getOc(stores,v);
        }
        return dtos;
    }
//    public List<AutoCompleteDTO> getStoreByPM(HttpSession session,HttpServletRequest request, HttpServletRequest req,
//                                              String v) {
//        User user = this.getUser(session);
//        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
//                Constants.SESSION_STORES);
//        log.debug("抓取店铺信息，user:{}", user.getUserId());
//        List<AutoCompleteDTO> dtos = null;
//        if(roleIds!=null&&roleIds.size()>0){
//            dtos = ma1000Service.getListByPM(roleIds,v);
//        }
//        return dtos;
//    }

    /**
     * 获取店铺下拉 (全部)
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getStoreAll")
    @ResponseBody
    public List<AutoCompleteDTO> getStoreAll(HttpSession session, HttpServletRequest req,
                                              String v) {
        User user = this.getUser(session);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = ma1000Service.getListAll(v);
        return dtos;
    }
    @RequestMapping(value = "/getSm")
    @ResponseBody
    public AjaxResultDto getStore(HttpSession session, HttpServletRequest req,String storeCd){
        AjaxResultDto res = new AjaxResultDto();
        res.setSuccess(false);
        if(StringUtils.isNotBlank(storeCd)){
            List<Ma1000> list =ma1000Service.selectStoreByStoreCd(storeCd);
            if (list!=null&&list.size()>0){
                res.setData(list);
                res.setSuccess(true);
            }else{
                res.setMessage("Get information is empty！");
            }
        }else{
            res.setMessage("Store No. is empty！");
        }
        return res;
    }



}
