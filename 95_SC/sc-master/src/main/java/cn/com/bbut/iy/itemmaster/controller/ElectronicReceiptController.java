package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ElectronicReceiptParam;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.SA0060;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.electronicReceipt.ElectronicReceiptService;
import cn.com.bbut.iy.itemmaster.util.DateConvert;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * 电子小票管理
 *
 * @author zcz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/electronicReceipt")
public class ElectronicReceiptController extends BaseAction {

//    @Autowired
//    private ElectronicReceiptService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private ElectronicReceiptService service;
    @Autowired
    private DefaultRoleService defaultRoleService;

    String content = "";
    int page = 0;
    /**
     * 电子小票管理画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_RECEIPTS_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 电子小票管理", u.getUserId());
        ModelAndView mv = new ModelAndView("electronicReceipt/electronicReceiptList");
        mv.addObject("useMsg", "电子小票管理画面");
        return mv;
    }
    // 查询小票类型
    @ResponseBody
    @RequestMapping(value = "/getReceiptType")
    public List<AutoCompleteDTO> getReceiptType(String v,String  storeCd,String posId,String startDate,String endDate){
        DateConvert dateConvert = new DateConvert();
        String StartDate = dateConvert.ChangeFormat4Date(startDate, "dd/MM/yyyy", "yyyyMMdd");
        String EndDate = dateConvert.ChangeFormat4Date(endDate, "dd/MM/yyyy", "yyyyMMdd");
        return service.getReceiptType(v, storeCd, posId,StartDate, EndDate);

    }
    @ResponseBody
    @RequestMapping(value = "/getSa0020Item")
    public List<AutoCompleteDTO> getSa0020Item(String v,String  storeCd,String posId,String startDate,String endDate){
        DateConvert dateConvert = new DateConvert();
        String StartDate = dateConvert.ChangeFormat4Date(startDate, "dd/MM/yyyy", "yyyyMMdd");
        String EndDate = dateConvert.ChangeFormat4Date(endDate, "dd/MM/yyyy", "yyyyMMdd");
        return service.getItemInfo(v, storeCd, posId,StartDate, EndDate);

    }
    /**
     * 查询小票信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData")
    @Permission(codes = { PermissionCode.CODE_SC_RECEIPTS_LIST_VIEW})
    public ReturnDTO getData(HttpServletRequest request, HttpSession session,
                                          String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        ElectronicReceiptParam param = gson.fromJson(searchJson, ElectronicReceiptParam.class);
        List<SA0060> _list = null;
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session,param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            _return.setMsg("Query result is empty!");
            return _return;
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setSaleStartDate(startDate);
        }
        param.setStores(stores);
        _list = service.getList(param);
        if(_list == null || _list.size() < 1){
            _return.setMsg("Query result is empty!");
            _return.setO(_list);
            _return.setSuccess(false);
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded");/*查询小票成功*/
        }
        return _return;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,ElectronicReceiptParam param){
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

    List<SA0060> _list = null;
    @RequestMapping(value = "/toPdf")
    @ResponseBody
    public ReturnDTO fileToPdf(HttpSession session, String searchJson, OutputStream outputStream) {

        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        ElectronicReceiptParam param = gson.fromJson(searchJson, ElectronicReceiptParam.class);

        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if (stores.size() == 0) {
            log.info(">>>>>>>>>>>>>> >>>>>>> get stores is null");
            return _return;
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setSaleStartDate(startDate);
        }
        param.setStores(stores);
        _list = service.getList(param);
        if (_list == null || _list.size() < 1) {
            _return.setMsg("Query result is empty!");
            _return.setO(_list);
            _return.setSuccess(false);
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
        }

        return _return;
    }

    @RequestMapping(value = "/writerPdf")
    public void fileAndPdf(HttpSession session, HttpServletResponse response){
        String fileName = "Electronic Receipts Query.pdf";
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);

            OutputStream outputStream = response.getOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            // 页面大小(A4纵向)
            Rectangle rectangle = new Rectangle(new RectangleReadOnly(595F, 842F));
            // 页面背景颜色
            rectangle.setBackgroundColor(BaseColor.WHITE);
            document.setPageSize(rectangle);
            // 页边距 左，右，上，下
            document.setMargins(5, 5, 5, 5);
            document.open();
            //设置字体
            //非汉字字体颜色
            Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9f);
            f1.setColor(BaseColor.BLACK);

            for (int i = 0; i < _list.size(); i++) {
                content = _list.get(i).getReceiptContent();
                Paragraph p = new Paragraph(content);
                document.newPage();
                document.add(p);
            }

            document.close();

            outputStream.flush();
            outputStream.close();
            } catch (IOException e) {
            log.info("<<<<<"+"导出pdf异常");
            e.printStackTrace();
            } catch (DocumentException e) {
            log.info("<<<<<"+"文件异常");
            e.printStackTrace();
        }
    }

}
