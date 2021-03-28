package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.PriceLabelMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0060;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.priceLabel.PriceLabelService;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.html.head.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 价签管理
 * @author ty
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/priceLabel")
public class PriceLabelController extends BaseAction {

    @Autowired
    private PriceLabelService priceLabelService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060ServiceImpl cm9060Service;
    @Autowired
    private PriceLabelMapper priceLabelMapper;

    @Value("${file.fileDir}")
    private String fileDir;//上传文件路径
    private final String EXCEL_EXPORT_NAME = "Price Label Management.xlsx";

    private final String EXCEL_EXPORT_KEY = "EXCEL_PRICE_LABLE";
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_PRICE_LABEL_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        User user = this.getUser(session);
        log.debug("User:{} 进入 价签打印画面", user.getUserId());

        ModelAndView mv = new ModelAndView("priceLabel/priceLabel");
        mv.addObject("useMsg", "价签打印画面");
        return mv;
    }

    /**
     * 跳转到打印页面
     */
    @RequestMapping("/print")
    @ResponseBody
    public ModelAndView toPrintView(String searchJson,String flg, HttpServletRequest request, HttpSession session,String params) {
        User user = this.getUser(session);
        log.debug("User:{} 进入 价签打印画面", user.getUserId());

        ModelAndView mv = new ModelAndView("priceLabel/priceLabelPrint");
        mv.addObject("useMsg", "价签打印画面");
        mv.addObject("searchJson", searchJson);
        mv.addObject("flg", flg);
        mv.addObject("params", params);
        return mv;
    }

    /**
     * 查询未来三天价签数据
     */
    @RequestMapping("/print/getPrintData")
    @ResponseBody
    public List<PriceLabelDTO> getPrintData(HttpServletRequest request, HttpSession session,String searchJson) {

        if (searchJson==null|| StringUtils.isEmpty(searchJson)) {
            return new ArrayList<PriceLabelDTO>();
        }

        Gson gson = new Gson();
        PriceLabelParamDTO param = gson.fromJson(searchJson, PriceLabelParamDTO.class);

        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ArrayList<PriceLabelDTO>();
        }

        param.setStores(stores);
        // 打印不需要分页
        param.setFlg(false);
        List<PriceLabelDTO> list = priceLabelService.getPrintData(param);

        if (list == null) {
            return new ArrayList<PriceLabelDTO>();
        }
        return list;
    }

    /**
     * 查询未来三天价签数据
     */
    @RequestMapping("/search")
    @ResponseBody
    public GridDataDTO<PriceLabelDTO> search(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        if (searchJson==null|| StringUtils.isEmpty(searchJson)) {
            return null;
        }
        Gson gson = new Gson();
        PriceLabelParamDTO param = gson.fromJson(searchJson, PriceLabelParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<PriceLabelDTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return priceLabelService.search(param);
    }

    /**
     * 获取 未来三天价签商品数据
     */
    @RequestMapping(value = "/getItemList")
    @ResponseBody
    public List<AutoCompleteDTO> getItemList(HttpSession session, HttpServletRequest request, HttpServletRequest req, String v) {
        return priceLabelService.getItemList(v);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, PriceLabelParamDTO param){
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
        // 只选择了一部分参数，生成查询参数，后台查询判断
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

    @RequestMapping("/export")
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        PriceLabelParamDTO priceLabelParamDTO = gson.fromJson(searchJson, PriceLabelParamDTO.class);
        Collection<String> stores = getStores(session, priceLabelParamDTO);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("priceLableExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

//    List<PriceLabelDTO> _list=null;
//    @RequestMapping(value = "/toPdf")
//    @ResponseBody
//    public ReturnDTO toPdf(HttpSession session, String searchJson, OutputStream outputStream){
//        ReturnDTO _return = new ReturnDTO();
//        if(StringUtils.isBlank(searchJson)){
//            _return.setMsg("Parameter cannot be empty!");
//            return _return;
//        }
//        Gson gson=new Gson();
//        PriceLabelParamDTO priceLabelParamDTO=gson.fromJson(searchJson,PriceLabelParamDTO.class);
//        Collection<String> stores = getStores(session, priceLabelParamDTO);
//        if(stores.size() == 0){
//            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
//            return null;
//        }
//        priceLabelParamDTO.setBusinessDate(cm9060Service.getValByKey("0000"));
//
//        priceLabelParamDTO.setStores(stores);
//        priceLabelParamDTO.setFlg(false);
//        _list= priceLabelMapper.search(priceLabelParamDTO);
//        if (_list == null || _list.size() < 1) {
//            _return.setMsg("Query result is empty!");
//            _return.setO(_list);
//            _return.setSuccess(false);
//        }else{
//            _return.setO(_list);
//            _return.setSuccess(true);
//        }
//
//        return _return;
//    }
    @RequestMapping(value = "/toPdf")
    public void fileAndPdf(HttpSession session, HttpServletResponse response,String searchJson){
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return;
        }

        Gson gson=new Gson();
        PriceLabelParamDTO priceLabelParamDTO=gson.fromJson(searchJson,PriceLabelParamDTO.class);
        Collection<String> stores = getStores(session, priceLabelParamDTO);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return;
        }
        priceLabelParamDTO.setFlg(false);
        priceLabelParamDTO.setStores(stores);
        priceLabelParamDTO.setBusinessDate(cm9060Service.getValByKey("0000"));
        List<PriceLabelDTO> _list = priceLabelMapper.search(priceLabelParamDTO);
        String fileName = "Price Lable Management.pdf";
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);

            OutputStream outputStream = response.getOutputStream();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            // 页面大小(A4纵向)
            Rectangle rectangle = new Rectangle(new RectangleReadOnly(842F, 595F));
            // 页面背景颜色
            rectangle.setBackgroundColor(BaseColor.WHITE);
            document.setPageSize(rectangle);
            // 页边距 左，右，上，下
            document.setMargins(5, 5, 5, 5);
            document.open();
            String path = fileDir + "/Calibri.ttf";;//自己的字体资源路径
            BaseFont bf = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(bf,24f,Font.BOLD,BaseColor.BLACK);
            Font subhead = new Font(bf,10f,Font.BOLD,BaseColor.BLACK);
            Font font = new Font(bf,10f,Font.NORMAL,BaseColor.BLACK);
            //设置字体
            //非汉字字体颜色
            // 段落
            Paragraph para = new Paragraph((" "), titleFont);
            para.setLeading(10f);
            para.setSpacingAfter(10f);
            document.add(para);
            // 标题
            Paragraph paragraph = new Paragraph(("Price Lable Management"), titleFont);
            paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph.setIndentationLeft(12); //设置左缩进
            paragraph.setIndentationRight(12); //设置右缩进
            paragraph.setFirstLineIndent(24); //设置首行缩进
            paragraph.setLeading(20f); //行间距
            paragraph.setSpacingBefore(60f); //设置段落上空白
            paragraph.setSpacingAfter(10f); //设置段落下空白
            document.add(paragraph);

            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);
            // 设置表格的宽度
        table.setTotalWidth(800);
            // 也可以每列分别设置宽度
            table.setTotalWidth(new float[]{ 45, 60, 85, 80, 65, 60, 65, 60, 60, 50, 45, 45,45,70 });

            // 锁住宽度
            table.setLockedWidth(true);
            // 设置表格上面空白宽度
            table.setSpacingBefore(10f);
            // 设置表格下面空白宽度
            table.setSpacingAfter(20f);
            table.getDefaultCell().setBorder(1);
            PdfContentByte cb = writer.getDirectContent();

            // 构建每个单元格
            PdfPCell cell1 = new PdfPCell(new Paragraph("Store No.", subhead));
            // 边框颜色
            cell1.setBorderColor(BaseColor.GRAY);
            // 背景颜色
            cell1.setBackgroundColor(BaseColor.WHITE);
            // 设置跨行
            cell1.setRowspan(0);
            // 设置距左边的距离
            cell1.setPaddingLeft(0);
            // 设置高度
            cell1.setFixedHeight(40);
            // 设置内容水平居中显示
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            // 设置垂直居中
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Paragraph("Store Name", subhead));
            cell2.setBorderColor(BaseColor.GRAY);
            cell2.setPaddingLeft(0);
            cell2.setFixedHeight(40);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell2);


            PdfPCell cell3 = new PdfPCell(new Paragraph("Price Label VN Name", subhead));
            cell3.setBorderColor(BaseColor.GRAY);
            cell3.setPaddingLeft(0);
            cell3.setFixedHeight(40);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell3);


            PdfPCell cell4 = new PdfPCell(new Paragraph("Price Label EN Name", subhead));
            cell4.setBorderColor(BaseColor.GRAY);
            cell4.setPaddingLeft(0);
            cell4.setFixedHeight(40);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell4);



            PdfPCell cell5 = new PdfPCell(new Paragraph("Top Department", subhead));
            cell5.setBorderColor(BaseColor.GRAY);
            cell5.setPaddingLeft(0);
            cell5.setFixedHeight(40);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Paragraph("Department", subhead));
            cell6.setBorderColor(BaseColor.GRAY);
            cell6.setPaddingLeft(0);
            cell6.setFixedHeight(40);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell6);

            PdfPCell cell7 = new PdfPCell(new Paragraph("Category", subhead));
            cell7.setBorderColor(BaseColor.GRAY);
            cell7.setPaddingLeft(0);
            cell7.setFixedHeight(40);
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell7);

            PdfPCell cell8 = new PdfPCell(new Paragraph("Sub Category", subhead));
            cell8.setBorderColor(BaseColor.GRAY);
            cell8.setPaddingLeft(0);
            cell8.setFixedHeight(40);
            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell8);

            PdfPCell cell9 = new PdfPCell(new Paragraph("Item Name", subhead));
            cell9.setBorderColor(BaseColor.GRAY);
            cell9.setPaddingLeft(0);
            cell9.setFixedHeight(40);
            cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell9);

            PdfPCell cell10 = new PdfPCell(new Paragraph("Item Barcode", subhead));
            cell10.setBorderColor(BaseColor.GRAY);
            cell10.setPaddingLeft(0);
            cell10.setFixedHeight(40);
            cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell10);

            PdfPCell cell11 = new PdfPCell(new Paragraph("Item Code", subhead));
            cell11.setBorderColor(BaseColor.GRAY);
            cell11.setPaddingLeft(0);
            cell11.setFixedHeight(40);
            cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell11);

            PdfPCell cell12 = new PdfPCell(new Paragraph("Old Price", subhead));
            cell12.setBorderColor(BaseColor.GRAY);
            cell12.setPaddingLeft(0);
            cell12.setFixedHeight(40);
            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell12);

            PdfPCell cell13 = new PdfPCell(new Paragraph("New Price", subhead));
            cell13.setBorderColor(BaseColor.GRAY);
            cell13.setPaddingLeft(0);
            cell13.setFixedHeight(40);
            cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell13);

            PdfPCell cell14 = new PdfPCell(new Paragraph("Effective Start Date", subhead));
            cell14.setBorderColor(BaseColor.GRAY);
            cell14.setPaddingLeft(0);
            cell14.setFixedHeight(40);
            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell14);
            for(int i = 0; i< _list.size();i++) {
                cell1 = new PdfPCell(new Paragraph(_list.get(i).getStoreCd(), font));
                cell1.setBorderColor(BaseColor.GRAY);
                cell1.setFixedHeight(40);
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell1);

                cell2 = new PdfPCell(new Paragraph(_list.get(i).getStoreName(), font));
                cell2.setFixedHeight(40);
                cell2.setBorderColor(BaseColor.GRAY);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell3 = new PdfPCell(new Paragraph(_list.get(i).getPriceLabelVnName(), font));
                cell3.setFixedHeight(40);
                cell3.setBorderColor(BaseColor.GRAY);
                cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell3);

                cell4 = new PdfPCell(new Paragraph(_list.get(i).getPriceLabelEnName(), font));
                cell4.setFixedHeight(40);
                cell4.setBorderColor(BaseColor.GRAY);
                cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell4);



                cell5 = new PdfPCell(new Paragraph(_list.get(i).getDepName(), font));
                cell5.setFixedHeight(40);
                cell5.setBorderColor(BaseColor.GRAY);
                cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell5);

                cell6 = new PdfPCell(new Paragraph(_list.get(i).getPmaName(), font));
                cell6.setFixedHeight(40);
                cell6.setBorderColor(BaseColor.GRAY);
                cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell6);

                cell7 = new PdfPCell(new Paragraph(_list.get(i).getCategoryName(), font));
                cell7.setFixedHeight(40);
                cell7.setBorderColor(BaseColor.GRAY);
                cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell7);

                cell8 = new PdfPCell(new Paragraph(_list.get(i).getSubCategoryName(), font));
                cell8.setFixedHeight(40);
                cell8.setBorderColor(BaseColor.GRAY);
                cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell8);

                cell9 = new PdfPCell(new Paragraph(_list.get(i).getArticleName(), font));
                cell9.setFixedHeight(40);
                cell9.setBorderColor(BaseColor.GRAY);
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell9);

                cell10 = new PdfPCell(new Paragraph(_list.get(i).getBarcode(), font));
                cell10.setFixedHeight(40);
                cell10.setBorderColor(BaseColor.GRAY);
                cell10.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell10);

                cell11 = new PdfPCell(new Paragraph(_list.get(i).getArticleId(), font));
                cell11.setFixedHeight(40);
                cell11.setBorderColor(BaseColor.GRAY);
                cell11.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell11);

                cell12 = new PdfPCell(new Paragraph(formatNum(String.valueOf(_list.get(i).getOldPrice())), font));
                cell12.setFixedHeight(40);
                cell12.setBorderColor(BaseColor.GRAY);
                cell12.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell12);

                cell13 = new PdfPCell(new Paragraph(formatNum(String.valueOf(_list.get(i).getNewPrice())), font));
                cell13.setFixedHeight(40);
                cell13.setBorderColor(BaseColor.GRAY);
                cell13.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell13);



                cell14 = new PdfPCell(new Paragraph(fmtDateToStr(_list.get(i).getEffectiveStartDate()), font));
                cell14.setFixedHeight(40);
                cell14.setBorderColor(BaseColor.GRAY);
                cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell14);
            }

            document.add(table);
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
    public static String fmtDateToStr(String date) {
        if (StringUtils.isBlank(date) || date.length()!=8) {
            return "";
        }
        return date.substring(6, 8) + "/" + date.substring(4, 6) + "/"
                + date.substring(0, 4);
    }
    public static String formatNum(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        int i = new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(i);
        return result;
    }

}

