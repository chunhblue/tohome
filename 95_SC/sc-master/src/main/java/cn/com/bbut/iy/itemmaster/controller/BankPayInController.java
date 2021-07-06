package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060GridDto;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060ParamDto;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.op0060.OP0060;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.OP0060Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 银行缴款
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/bankPayIn")
public class BankPayInController extends BaseAction {
    @Autowired
    private OP0060Service op0060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private DefaultRoleService defaultRoleService;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_DEPOSIT_JOURNAL";
    private final String EXCEL_EXPORT_NAME = "Deposit Journal Query.xlsx";

    /**
     * 银行缴款画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PI_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        log.debug("User:{} 进入 银行缴款", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("bankPayIn/bankPayIn");
        mv.addObject("useMsg", "银行缴款画面");
        mv.addObject("businessDate", date);
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取缴费信息列表
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<OP0060GridDto> getPayList(HttpServletRequest request, HttpSession session,
                                                 Map<String, ?> model, OP0060ParamDto paramDto) {
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, paramDto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<OP0060GridDto>();
        }
        User u = this.getUser(session);
        /*int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            paramDto.setStartDate(startDate);
        }*/
        paramDto.setStores(stores);
        GridDataDTO<OP0060GridDto> grid = op0060Service.getList(paramDto);
        return grid;
    }

    /**
     * 保存银行缴款信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_PI_VIEW,
            PermissionCode.CODE_SC_PI_EDIT,
    })
    public ResultDto save(HttpServletRequest request, HttpSession session,
                          Map<String, ?> model, OP0060 param , String flg) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        param.setUpdateUserId(u.getUserId());
        param.setUpdateYmd(ymd);
        param.setUpdateHms(hms);
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(param!=null&&StringUtils.isNotBlank(flg)&&
                StringUtils.isNotBlank(param.getStoreCd())&&
                StringUtils.isNotBlank(param.getAccDate())&&
                StringUtils.isNotBlank(param.getPayPerson())
        ){
            if("add".equals(flg)){
                param.setCreateUserId(u.getUserId());
                param.setCreateYmd(ymd);
                param.setCreateHms(hms);
            }
            try {
                int result = op0060Service.insertOrUpdate(param,flg);
                if(result>0){
                    resultDto.setSuccess(true);
                    resultDto.setMessage("Data saved successfully!");
                }else{
                    resultDto.setMessage("Data saved failed!");
                }
            }catch (Exception e){
                resultDto.setSuccess(false);
                resultDto.setMessage("Data saved failed!");
            }
        }else{
            resultDto.setMessage("Parameter cannot be empty!");
        }
        return resultDto;
    }


    @ResponseBody
    @RequestMapping(value = "/searchStoresInsert")
    public AjaxResultDto searhSameDayInsert(OP0060ParamDto param){
        AjaxResultDto ajaxResultDto = new AjaxResultDto();
        Integer items= op0060Service.searhSameDayInsert(param);
       if (items>=2){
           Integer nextItem=items+1;
           ajaxResultDto.setData(items);
           ajaxResultDto.setSuccess(true);
           ajaxResultDto.setS("deposit data "+' '+items+' '+"records already exists in system, are you sure to enter the Next one?");
           return  ajaxResultDto;
       }else {
           return  ajaxResultDto;
       }
    }


    /**
     * 删除银行缴款信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete")
    @Permission(codes = {
            PermissionCode.CODE_SC_PI_DEL,
    })
    public ResultDto delete(HttpServletRequest request, HttpSession session,
                            Map<String, ?> model, OP0060 param , String flg) {
        User u = this.getUser(session);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        param.setUpdateUserId(u.getUserId());
        param.setUpdateYmd(sdf.format(date));
        param.setUpdateHms(sdf1.format(date));
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(param!=null&&StringUtils.isNotBlank(param.getStoreCd())&&
                StringUtils.isNotBlank(param.getAccDate())&&
                param.getDeltaNo()!=null&&param.getDeltaNo()>0
        ){
            try {
                int result = op0060Service.deleteByParam(param);
                if(result>0){
                    resultDto.setSuccess(true);
                    resultDto.setMessage("Deleted succeed!");
                }else{
                    resultDto.setMessage("Deleted failed!");
                }
            }catch (Exception e){
                resultDto.setSuccess(false);
                resultDto.setMessage("Deleted failed!");
            }
        }else{
            resultDto.setMessage("Parameters are not in correct format!");
        }
        return resultDto;
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_PI_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        OP0060ParamDto param = gson.fromJson(searchJson, OP0060ParamDto.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        User u = this.getUser(session);
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setUserId(u.getUserId());
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_PI_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("depositJournalExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    @RequestMapping(value = "/toPdf")
    public void fileAndPdf(HttpSession session, HttpServletResponse response,String searchJson){
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return;
        }
        Gson gson = new Gson();
        OP0060ParamDto param = gson.fromJson(searchJson, OP0060ParamDto.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return;
        }
        User u = this.getUser(session);
        /*int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setStartDate(startDate);
        }*/
        param.setStores(stores);

        String fileName = "Bank Deposit.pdf";
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);

            OutputStream outputStream = response.getOutputStream();

            // 获取需要导出的table信息
            op0060Service.depositList(param,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.info("<<<<<"+"导出pdf异常");
            e.printStackTrace();
        }
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,OP0060ParamDto param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
                && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(StringUtils.isNotBlank(param.getStoreCd())){
            stores.add(param.getStoreCd());
            return stores;
        }
        // 只选择了一部分参数，生成查询参数，后台查询
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
                stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }
}
