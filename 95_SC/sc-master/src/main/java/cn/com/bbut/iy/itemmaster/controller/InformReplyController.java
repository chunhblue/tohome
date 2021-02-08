package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.*;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.MA4315;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4300Service;
import cn.com.bbut.iy.itemmaster.service.inform_reply.Ma4310Service;
import cn.com.bbut.iy.itemmaster.service.inform_reply.Ma4315Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 通知查看及回复
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/informReply")
public class InformReplyController extends BaseAction {
    @Autowired
    private Ma4300Service ma4300Service;
    @Autowired
    private Ma4315Service ma4315Service;
    @Autowired
    private Ma4310Service ma4310Service;

    /**
     * 通知回复管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_INF_REPLY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 通知回复管理", u.getUserId());
        ModelAndView mv = new ModelAndView("inform_reply/informReplyList");
        mv.addObject("useMsg", "通知回复管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 通知回复管理头档一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getReplyList")
    @ResponseBody
    public GridDataDTO<Ma4310DetailGridDto> getList(HttpServletRequest request, HttpSession session,
                                                    Ma4310DetailParamDto param) {
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取店铺cd集合
        Collection<String> storeCds = (Collection<String>) request.getSession().getAttribute(
                Constants.SESSION_STORES);
        if(param == null){
            param = new Ma4310DetailParamDto();
        }
        param.setUserId(this.getUserId(request.getSession()));
        param.setStoreList(storeCds);
        param.setRoleList(roleIds);
        return ma4310Service.getReplyList(param);
    }

    /**
     * 通知回复管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_INF_REPLY_VIEW,
            PermissionCode.CODE_SC_INF_REPLY_REPLY
    })
    public ModelAndView orderDetailsAdd(HttpServletRequest request, HttpSession session,
                                        Map<String, ?> model,String operateFlg,
                                        String informCd,String storeCd) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 通知回复管理详情画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("inform_reply/informReplyEdit");
        mv.addObject("operateFlg", operateFlg);
        mv.addObject("informCd", informCd);
        mv.addObject("storeCd", storeCd);
        mv.addObject("useMsg", "通知回复管理详情画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 通知回复信息保存
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit/reply")
    @Permission(codes = { PermissionCode.CODE_SC_INF_REPLY_REPLY})
    public AjaxResultDto informReply(HttpServletRequest request, HttpSession session,
                                     MA4315 param) {
        User user = this.getUser(session);
        log.debug("保存通知回复消息，user:{}", user.getUserId());
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if(param==null||
            StringUtils.isBlank(param.getInformCd())||
            StringUtils.isBlank(param.getStoreCd())||
            StringUtils.isBlank(param.getInformReplyContent())){
            ard.setSuccess(false);
            ard.setMessage("Save failed! Parameter cannot be empty!");
            return ard;
        }
        boolean result = false;
        param.setReplyUserId(user.getUserId());
        param.setInformReplyDate(date);
        param.setCreateUserId(user.getUserId());
        param.setCreateYmd(ymd);
        param.setCreateHms(hms);
        int flg = ma4315Service.insertInformReply(param);
        if(flg>0){
            result = true;
        }
        ard.setSuccess(result);
        return ard;
    }

    /**
     * 通知回复详细信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit/getInformReply")
    public ResultDto getInformReply(HttpSession session, String informCd,String storeCd) {
        ResultDto res = new ResultDto();
        res.setSuccess(false);
        if(StringUtils.isNotBlank(informCd)&&
                StringUtils.isNotBlank(storeCd)){
            Ma4310ResultDto dto = ma4310Service.getInformReply(informCd,storeCd);
            if (dto!=null){
                res.setData(dto);
                res.setSuccess(true);
            }else{
                res.setMessage("Get information is empty！");
            }
        }else{
            res.setMessage("Parameter is empty!");
        }
        return res;
    }

    /**
     * 通知回复详细信息一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getReplyDetailList")
    @ResponseBody
    public GridDataDTO<Ma4315DetailGridDto> getReplyDetailList(HttpServletRequest request, HttpSession session,
                                                    Ma4315DetailParamDto param) {
        User user = this.getUser(session);
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        if(param==null||StringUtils.isBlank(param.getInformCd())){
            return new GridDataDTO<>();
        }
        param.setUserId(user.getUserId());
        return ma4315Service.getReplyList(param,roleIds);
    }

}
