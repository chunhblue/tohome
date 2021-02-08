package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.service.ma2000.Ma2000Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * 供应商信息
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma2000")
public class Ma2000Controller extends BaseAction {
    @Autowired
    private Ma2000Service ma2000Service;

    /**
     * 获取供应商下拉
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getVendorAuto")
    @ResponseBody
    public List<AutoCompleteDTO> getVendorList(HttpSession session, HttpServletRequest req,
                                              String v,String storeCd) {
        User user = this.getUser(session);
        log.debug("抓取供应商信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = ma2000Service.getListAll(v,storeCd);
        return dtos;
    }

    /**
     * 获取供应商email，最低订货金额信息..
     * @param session
     * @param req
     * @param vendorId
     * @return
     */
    @RequestMapping(value = "/getVendorById")
    @ResponseBody
    public AjaxResultDto getVendorById(HttpSession session, HttpServletRequest req,
                                       String vendorId) {
        User user = this.getUser(session);
        log.debug("抓取供应商信息，user:{}", user.getUserId());
        AjaxResultDto resultDto = new AjaxResultDto();
        if (StringUtils.isBlank(vendorId)){
            resultDto.setMessage("Parameter cannot be empty!");
            resultDto.setSuccess(false);
        }else{
            MA2000 ma2000 = ma2000Service.getVendorById(vendorId);
            if(ma2000!=null){
                resultDto.setData(ma2000);
                resultDto.setSuccess(true);
            }else{
                resultDto.setSuccess(false);
                resultDto.setMessage("Failed to load supplier data!");
            }
        }
        return resultDto;
    }

    @RequestMapping(value = "/getVendorList")
    @ResponseBody
    public List<AutoCompleteDTO> getVendor(HttpSession session, HttpServletRequest req,String v){
        User user = this.getUser(session);
        log.debug("获取供应商信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> vendorlist=ma2000Service.getAllVendor(v);
        return  vendorlist;
    }
}
