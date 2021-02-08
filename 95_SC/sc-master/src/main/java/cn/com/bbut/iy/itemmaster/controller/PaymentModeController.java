package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0005DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0005.SA0005ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0000;
import cn.com.bbut.iy.itemmaster.entity.SA0005;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160;
import cn.com.bbut.iy.itemmaster.service.sa0000.SA0000Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.loadtime.Aj;
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
import java.util.List;
import java.util.Map;

/**
 * POS支付方式设定
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/paymentMode")
public class PaymentModeController extends BaseAction {
    @Autowired
    private SA0000Service sa0000Service;

    /**
     * POS支付方式设定画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PM_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 POS支付方式设定", u.getUserId());
        ModelAndView mv = new ModelAndView("paymentMode/paymentMode");
        mv.addObject("useMsg", "POS支付方式设定画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_POS_PAYMENT);
        this.saveToken(request);
        return mv;
    }

    /**
     * POS支付方式设定画面新增修改
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_PM_EDIT,
            PermissionCode.CODE_SC_PM_ADD,
            PermissionCode.CODE_SC_PM_VIEW
    })
    public ModelAndView orderDetailsAdd(HttpServletRequest request, HttpSession session,
                                        String operateFlg,String payCd,String auditFlag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 POS支付方式设定新增修改画面", u.getUserId());
        ModelAndView mv = new ModelAndView("paymentMode/paymentModeEdit");
        mv.addObject("operateFlg", operateFlg);
        mv.addObject("payCd", payCd);
        mv.addObject("userName",u.getUserName());
        mv.addObject("auditFlag",auditFlag);
        mv.addObject("useMsg", "POS支付方式设定新增修改管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_POS_PAYMENT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_POS_PAYMENT);
        mv.addObject("userId",  u.getUserId());
        this.saveToken(request);
        return mv;
    }

    /**
     * POS支付方式设定头档一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<SA0000DetailGridDto> getList(HttpServletRequest request, HttpSession session,
                                                    SA0000DetailParamDto param) {
        return sa0000Service.getList(param);
    }

    @ResponseBody
    @RequestMapping(value = "/edit/getPosId")
    public AjaxResultDto getPosId(HttpServletRequest request,HttpSession session, String storeCd) {
        AjaxResultDto res = new AjaxResultDto();
        res.setSuccess(false);
        if(StringUtils.isNotBlank(storeCd)){
            List<SA0160> list = sa0000Service.getPosId(storeCd);
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
//    @ResponseBody
//    @RequestMapping(value = "/edit/deleRecord")
//    public AjaxResultDto deleRecord(HttpServletRequest request, HttpSession session,String delJson) {
//        AjaxResultDto ajaxResultDto = new AjaxResultDto();
//        if (delJson != null) {
//            // 删除已经有的数据
//            List<SA0005ParamDTO> sa0005List = new Gson().fromJson(delJson, new TypeToken<List<SA0005ParamDTO>>() {
//            }.getType());
//            if (sa0005List != null) {
//                for (int i = 0; i < sa0005List.size(); i++) {
//                    SA0005ParamDTO sa0005 = sa0005List.get(i);
//                    sa0000Service.deleteSa005(sa0005);
//                }
//            }
//        }
//        ajaxResultDto.setSuccess(true);
//        return  ajaxResultDto;
//    }
    /**
     *删掉中间库所有数据
     */

    @ResponseBody
    @RequestMapping(value = "/edit/deleAllRecord")
    public AjaxResultDto deleAllRecord(HttpServletRequest request, HttpSession session,SA0005ParamDTO sa0005ParamDTO) {
        AjaxResultDto ajaxResultDto = new AjaxResultDto();
        //获取中间表数据
        List<SA0005ParamDTO> sa0005ParamDTOS = sa0000Service.getdeldetailList(sa0005ParamDTO.getPayCd());
        for (SA0005ParamDTO sa005: sa0005ParamDTOS) {
            if (!sa005.getRecordStatus().equals("delete")){
                sa0000Service.inserttoSa0005(sa005);
            }
            // 在原有数据库中删除 中间删除标记信息
            if (sa005.getRecordStatus().equals("delete")){
                sa0000Service.deleteSa005(sa005);
            }
        }
        int count = sa0000Service.deleteSa005(sa0005ParamDTO);
        if (count !=0){
            ajaxResultDto.setSuccess(true);
        }
        return  ajaxResultDto;
    }

    /**
     * POS支付方式保存
     * @param request
     * @param session
     * @return
     */

    @RequestMapping(value = "/edit/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PM_EDIT,
            PermissionCode.CODE_SC_PM_ADD,
    })
    public AjaxResultDto save(HttpServletRequest request, HttpSession session,
                          String paymentModeDetailJson, SA0000 param, String operateFlg) {

        User user = this.getUser(session);
        log.debug("保存POS支付方式信息，user:{}", user.getUserId());
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            return res;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);

//        //添加pos支付方式明细信息
//        if (delJsonParam!=null) {
//            // 删除已经有的数据
//          SA0005ParamDTO sa0005ParamDTO = new SA0005ParamDTO();
//            sa0000Service.deleteSa005(sa0005ParamDTO);
//            List<SA0005ParamDTO> sa0005List = new Gson().fromJson(delJsonParam, new TypeToken<List<SA0005ParamDTO>>() {}.getType());
//            if (sa0005List != null) {
//                for (int i = 0; i < sa0005List.size(); i++) {
//                    SA0005ParamDTO sa0005 = sa0005List.get(i);
//                    sa0000Service.insertSa0005(sa0005);
//                }
//            }
//     }
        if(param==null&&StringUtils.isBlank(operateFlg)||
                StringUtils.isBlank(param.getPayName())||
                StringUtils.isBlank(param.getPayNamePrint())||
                StringUtils.isBlank(param.getInterfaceSts())){
            res.setSuccess(false);
            res.setMessage("Save failed! Parameter cannot be empty!");
            return res;
        }
        param.setUpdateUserId(user.getUserId());
        param.setUpdateYmd(ymd);
        param.setUpdateHms(hms);
        String toKen = res.getToKen();
        try {
            if("add".equals(operateFlg)){
                param.setCreateUserId(user.getUserId());
                param.setCreateYmd(ymd);
                param.setCreateHms(hms);
                // 全部数据保存在中间表
                res = sa0000Service.insertPaymentMode(param,paymentModeDetailJson);
            }else if("edit".equals(operateFlg)){
                // 更新部分 recordStatus 为空的
                res = sa0000Service.updatePaymentMode(param,paymentModeDetailJson);
                //删除中间数据库
                 SA0005ParamDTO sa0005ParamDTO = new SA0005ParamDTO();
                sa0000Service.deleteSa005(sa0005ParamDTO);
                //  状态改变的数据全部数据保存在中间表
                sa0000Service.insertoSa0005(paymentModeDetailJson);
            }
        }catch (Exception e){
            e.printStackTrace();
            res.setMessage("Data saved failed!");
            res.setSuccess(false);
        }
        res.setToKen(toKen);
        return res;
    }

    /**
     * POS支付方式设定头档信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "edit/getPaymentInfo")
    public AjaxResultDto getPaymentInfo(HttpSession session, String payCd) {
        AjaxResultDto res = new AjaxResultDto();
        res.setSuccess(false);
        if(StringUtils.isNotBlank(payCd)){
            SA0000 dto = sa0000Service.getPaymentInfo(payCd);
            if (dto!=null){
                res.setData(dto);
                res.setSuccess(true);
            }else{
                res.setMessage("Get information is empty！");
            }
        }else{
            res.setMessage("Payment No. cannot be empty!");
        }
        return res;
    }
    @RequestMapping(value = "/edit/getDelList")
    @ResponseBody
    public GridDataDTO<SA0005DetailGridDto> getDelList(HttpServletRequest request, HttpSession session,
                                                       String payCd){

        GridDataDTO<SA0005DetailGridDto> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(payCd))
            grid.setRows(sa0000Service.getDeldetailList(payCd));
        return grid;
    }
    @RequestMapping(value = "/edit/getdelList")
    @ResponseBody
    public   List<SA0005ParamDTO>  getdelList(HttpServletRequest request, HttpSession session,
                                                       String payCd){
        List<SA0005ParamDTO> sa0005ParamDTOS=null;
        if(StringUtils.isNotBlank(payCd)) {
            sa0005ParamDTOS = sa0000Service.getdeldetailList(payCd);
            return sa0005ParamDTOS;
        }
        return sa0005ParamDTOS;
    }
    /**
     * POS支付方式设定详细一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit/getList")
    @ResponseBody
    public GridDataDTO<SA0005DetailGridDto> getDetailList(HttpServletRequest request, HttpSession session,
                                                          String payCd) {
        GridDataDTO<SA0005DetailGridDto> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(payCd))
        grid.setRows(sa0000Service.getDetailList(payCd));
        return grid;
    }

    /**
     * 获取店铺信息（From SA0160）
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit/getStore")
    public List<AutoCompleteDTO> getData(HttpServletRequest request, HttpSession session, String v) {
        return sa0000Service.getStoreList(v);
    }
    @ResponseBody
    @RequestMapping(value = "/edit/savedel")
    public Integer savedel(HttpServletRequest request, HttpSession session,SA0005ParamDTO sa0005){
        int count = sa0000Service.insertSa0005(sa0005);
        return count;
    }
    @ResponseBody
    @RequestMapping(value = "/edit/savedeltosa0005")
    public AjaxResultDto savedeltosa0005(HttpServletRequest request, HttpSession session,String  delJson){
        List<SA0005ParamDTO> sa0005List = new Gson().fromJson(delJson, new TypeToken<List<SA0005ParamDTO>>() {}.getType());
        if (sa0005List != null) {
            for (int i = 0; i < sa0005List.size(); i++) {
                SA0005ParamDTO sa0005 = sa0005List.get(i);
                 sa0000Service.inserttoSa0005(sa0005);
            }

        }
        AjaxResultDto ajaxResultDto = new AjaxResultDto();
        ajaxResultDto.setSuccess(true);
       return  ajaxResultDto;
    }
}
