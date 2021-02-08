package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma4170.MA4170;
import cn.com.bbut.iy.itemmaster.service.ma4170.Ma4170Service;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 门店原因
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeReason")
public class Ma4170Controller extends BaseAction {
    @Autowired
    private Ma4170Service ma4170Service;
    /**
     * 门店原因画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_SR_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toPriceChangeEntey(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 门店原因", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storeReason/storeReason");
        mv.addObject("useMsg", "门店原因画面");
        return mv;
    }

    /**
     * 校验唯一
     */
    @RequestMapping(value = "/checkUnique")
    @ResponseBody
    public ReturnDTO checkUnique(String reasonCd, HttpServletRequest request, HttpSession session) {
        return ma4170Service.checkUnique(reasonCd);
    }

    /**
     * 门店原因一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<Ma4170DetailGridDto> getList(HttpServletRequest request, HttpSession session,
                                                                 Ma4170DetailParamDto param) {
        return ma4170Service.getList(param);
    }

    /**
     * 保存门店原因
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_SR_ADD,
            PermissionCode.CODE_SC_SR_EDIT,
    })
    public ResultDto save(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, MA4170 ma4170,String operateFlg) {
        User u = this.getUser(session);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(operateFlg)&&ma4170!=null&&
            StringUtils.isNotBlank(ma4170.getReasonCd())&&
            StringUtils.isNotBlank(ma4170.getReasonName())&&
            StringUtils.isNotBlank(ma4170.getReasonType())){
            int flg = 0;
            String operateMsg = "";
            if("1".equals(operateFlg)){
                operateMsg ="新增";
                MA4170 rs = ma4170Service.selectMa4170(ma4170);
                if(rs!=null){
                    resultDto.setMessage("Current Store reason already exists!");
                    return resultDto;
                }
                ma4170.setCreateUserId(u.getUserId());
                ma4170.setCreateYmd(ymd);
                ma4170.setCreateHms(hms);
                flg = ma4170Service.insertMa4170(ma4170);
            }else if("2".equals(operateFlg)){
                operateMsg ="修改";
                ma4170.setUpdateUserId(u.getUserId());
                ma4170.setUpdateYmd(ymd);
                ma4170.setUpdateHms(hms);
                flg = ma4170Service.updateMa4170(ma4170);
            }
            if(flg>0){
                resultDto.setSuccess(true);
            }else{
                resultDto.setMessage(operateMsg+"failed!");
            }
        }else{
            resultDto.setMessage("Parameter cannot be empty!");
        }
        return resultDto;
    }

    /**
     * 删除门店原因
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete")
    @Permission(codes = { PermissionCode.CODE_SC_SR_DEL})
    public ResultDto delete(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String reasonCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(reasonCd)){
            int flg = ma4170Service.deleteMa4170(reasonCd);
            if(flg>0){
                resultDto.setSuccess(true);
            }else{
                resultDto.setMessage("Deleted failed!");
            }
        }else{
            resultDto.setMessage("Reason No. cannot be empty!");
        }
        return resultDto;
    }
}
