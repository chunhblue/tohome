package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Cm9010共通查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cm9010")
public class Cm9010Controller extends BaseAction {

    @Autowired
    private Cm9010Service cm9010Service;

    @RequestMapping(value = "/getCode")
    @ResponseBody
    public List<Cm9010> getDpt(HttpSession session, HttpServletRequest req, String codeValue) {
        if(StringUtils.isBlank(codeValue)){
            return null;
        }
        return cm9010Service.getList(codeValue);
    }
    @RequestMapping(value = "/getReasonCode")
    @ResponseBody
    public List<AutoCompleteDTO> getResonValue(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getReason(v);
        return  reason;
    }

    @RequestMapping(value = "/getAllReasonCode")
    @ResponseBody
    public List<AutoCompleteDTO> getAllResonValue(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getAllReason(v);
        return  reason;
    }

    @RequestMapping(value = "/getWriteOffReasonValue")
    @ResponseBody
    public List<AutoCompleteDTO> getResonWriteOffValue(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getWriteOffReasonValue(v);
        return  reason;
    }
    // 店间调拨数量不一致原因
    @RequestMapping(value = "/getDifferenceReason")
    @ResponseBody
    public List<AutoCompleteDTO> getDifferenceReason(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getDiffReason(v);
        return  reason;
    }

    // 退货数量不一致原因
    @RequestMapping(value = "/getReturnDifferReason")
    @ResponseBody
    public List<AutoCompleteDTO> getReturnDifferReason(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getReturnDifferReason(v);
        return  reason;
    }

    // 收货数量与订货不一致原因
    @RequestMapping(value = "/getReceiptDifferReason")
    @ResponseBody
    public List<AutoCompleteDTO> getReceiptDifferReason(HttpSession session, HttpServletRequest req,String v){
        List<AutoCompleteDTO> reason = cm9010Service.getReceiptDifferReason(v);
        return  reason;
    }
}
