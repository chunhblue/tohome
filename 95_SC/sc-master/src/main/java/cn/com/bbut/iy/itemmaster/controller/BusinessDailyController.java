package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionSaleDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.businessDaily.BusinessDailyService;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
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
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 营业日报管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/businessDaily")
public class BusinessDailyController extends BaseAction {
    @Autowired
    private BusinessDailyService service;
    @Autowired
    private CM9060ServiceImpl cm9060Service;
    @Autowired
    private DefaultRoleService defaultRoleService;
    @Autowired
    private Ma4320Service ma4320Service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_STORE_OPERATION_DAILY";
    private final String EXCEL_EXPORT_NAME = "Store Operation Daily Report.xlsx";

    /**
     * 营业日报管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_BD_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        log.debug("User:{} 进入 营业日报管理", u.getUserId());
        SimpleDateFormat sp=new SimpleDateFormat("yyyyMMdd");
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        Date bsDate = new Date();
        try {
            bsDate = sp.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String checkFlg = "0";
        /*int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            checkFlg = "1";
        }*/
        ModelAndView mv = new ModelAndView("businessDaily/businessDaily");
        mv.addObject("useMsg", "营业日报管理画面");
        mv.addObject("checkFlg", checkFlg);
        mv.addObject("bsDate", bsDate);
        mv.addObject("printTime", Utils.getFormateDate(ymd));
        return mv;
    }

    /**
     * 获取营业员名称
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getData")
    public ResultDto getData(HttpServletRequest request, HttpSession session,
                             Map<String, ?> model, String storeCd ,String businessDate,String searchJson) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        User u = this.getUser(session);
        if(StringUtils.isNotBlank(storeCd)){
            int i = defaultRoleService.judgeSMPosition(u.getUserId(),storeCd);
            if(i==0){
                // 判断cash balance entry是否已经录入
               int num = service.getCashBalanceCount(businessDate,storeCd);
               if(num<1){
                   resultDto.setFlag("2"); // 没有数据
                   resultDto.setMessage("Please complete cash balancing entry for target store & date first!");
                   return resultDto;
               }
            }

            Map map = service.getData(storeCd,businessDate);
            if(map!=null){
                map.put("nowDate",new Date());
                resultDto.setSuccess(true);
                resultDto.setData(map);
                resultDto.setFlag("0");  // 查询成功
            }else{
                resultDto.setFlag("1"); // 没有数据
                resultDto.setMessage("The query data is empty!");
            }
        }else{
            resultDto.setMessage("Store No. cannot be empty!");
        }
        return resultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/getPayName")
    public ResultDto getPayName(HttpServletRequest request, HttpSession session,
                             Map<String, ?> model,String storeCd ,String businessDate) {
        ResultDto resultDto = new ResultDto();
        BusinessDailyDto dto = service.getPayInAmt(businessDate,storeCd);

        resultDto.setData(dto);
        resultDto.setSuccess(true);
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
    @Permission(codes = { PermissionCode.CODE_SC_BD_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(org.springframework.util.StringUtils.isEmpty(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_BD_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("businessDailyExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
}
