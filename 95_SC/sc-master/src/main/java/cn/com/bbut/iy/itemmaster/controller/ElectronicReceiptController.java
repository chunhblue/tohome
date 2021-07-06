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
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.util.DateConvert;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
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
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Value("${file.fileDir}")
    private String fileDir;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

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
//        File path = new File(imgUrl);
//        String imagePath = imgUrl+File.separator +"electronic.png";
        String businessDate = cm9060Service.getValByKey("0000");
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        log.debug("User:{} 进入 电子小票管理", u.getUserId());
        ModelAndView mv = new ModelAndView("electronicReceipt/electronicReceiptList");
        mv.addObject("useMsg", "电子小票管理画面");
        mv.addObject("businessDate", businessDate);
        mv.addObject("position", i);
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
    public List<AutoCompleteDTO> getSa0020Item(String v,String  storeCd,String posId,String saleDate){
        DateConvert dateConvert = new DateConvert();
        String salesDate = dateConvert.ChangeFormat4Date(saleDate, "dd/MM/yyyy", "yyyyMMdd");
        return service.getItemInfo(v, storeCd, posId,salesDate);

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
            Rectangle rectangle = new Rectangle(new RectangleReadOnly(260, 1100));
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
                document.newPage();
                //创建一个image对象.
                 Image img = Image.getInstance(fileDir+File.separator+"electronic.png");     //图片
                img.scaleAbsolute(180,70);//图片大小
//                image.setAbsolutePosition(50, 1110);//图片的X轴和Y轴
                img.setAlignment(1);

                document.add(img);
                content = _list.get(i).getReceiptContent();
                String cont = content.split("LOGO")[1];
                String[] arrs = cont.split("\n");
                for(int j=0;j<arrs.length;j++){
                    if("$ExtraInfo".equals(arrs[j]) || "$CUT_PAPER".equals(arrs[j])){
                        arrs[j] = "";
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String pig = sdf.format(new Date());
                    String year = pig.substring(0,4);
                    String mon = pig.substring(4,6);
                    String path = File.separator+year+File.separator+mon+File.separator+pig+".jpg";
                    if(arrs[j].contains("$ReceiptBarcode") && (arrs[j+1]!=null || !"".equals(arrs[j+1]))){
                        arrs[j] = "";
                       String imgPath = this.getBarCode(arrs[j+1],path);
                        //创建一个image对象.
                        Image barcode = Image.getInstance(imgPath);
                        barcode.scaleAbsolute(160,60);//图片大小
                        barcode.setAlignment(1);
                        document.add(barcode);
                        arrs[j+1] = "";
                        continue;
                    }
                    if(arrs[j].contains("$QRCode") && (arrs[j+1]!=null || !"".equals(arrs[j+1]))){
                        arrs[j] = "";
                        this.getCore(arrs[j+1],path);
                        //创建一个image对象.
                        Image core = Image.getInstance(path);
                        core.scaleAbsolute(120,120);//图片大小
                        core.setAlignment(1);
                        document.add(core);
                        arrs[j+1] = "";
                        continue;
                    }
                    Paragraph p = this.getFontByInfo(arrs[j]);
                    document.add(p);
                }
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

    public Paragraph getFontByInfo(String cont){
        Paragraph p = new Paragraph();
        //设置字体
        //非汉字字体颜色
        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10f);
        f1.setColor(BaseColor.BLACK);
        if(cont.contains("{Center;Bold}")){
            cont = cont.replace("{Center;Bold}","");
            f1.setStyle(Font.BOLD);
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_CENTER);
        }else if(cont.contains("{Bold;Center}")){
            cont = cont.replace("{Bold;Center}","");
            f1.setStyle(Font.BOLD);
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_CENTER);
        }else if(cont.contains("{Center}")){
            cont = cont.replace("{Center}","");
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_CENTER);
        }else if(cont.contains("{Bold}")){
            cont = cont.replace("{Bold}","");
            f1.setStyle(Font.BOLD);
            p = new Paragraph(cont,f1);
        }else if(cont.contains("{Bold;Right}")){
            cont = cont.replace("{Bold;Right}","");
            f1.setStyle(Font.BOLD);
            if(cont.contains("Total Items(s) Qty:")){
                cont = cont.replace("Total Items(s) Qty:","Total Items(s) Qty:          ");
            }else if(cont.contains("Total(+VAT):")){
                cont = cont.replace("Total(+VAT):","Total(+VAT):    ");
            }else if(cont.contains("CHANGE DUE:")){
                cont = cont.replace("CHANGE DUE:","CHANGE DUE:    ");
            }
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_RIGHT);
        }else if(cont.contains("{Right}")){
            cont = cont.replace("{Right}","");
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_RIGHT);
        }else if(cont.contains("{Right;Bold;Height}")){
            cont = cont.replace("{Right;Bold;Height}","");
            f1.setStyle(Font.BOLD);
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_RIGHT);
        }else if(cont.contains("{Right;Bold}")){
            cont = cont.replace("{Right;Bold}","");
            f1.setStyle(Font.BOLD);
            p = new Paragraph(cont,f1);
            p.setAlignment(Element.ALIGN_RIGHT);
        }else if(cont.contains("-------")){
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12f);
            cont = cont.replace("-------","--------------");
            cont = cont.substring(0,62);
            p = new Paragraph(cont,f2);
        }else if(cont.contains("=======")){
            Font f3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12f);
            cont = cont.replace("=======","==============");
            cont = cont.substring(0,36);
            p = new Paragraph(cont,f3);
        }else {
            if(cont.contains("Subtotal:")){
                cont = cont.replace("Subtotal:","Subtotal:    ");
            } else if(cont.contains("Cash:")){
                cont = cont.replace("Cash:","Cash:    ");
            }
            p = new Paragraph(cont,f1);
        }
        return p;
    }

    /**
     * 生成条形码具体实现
     * @param msg
     * @param path
     */
    public String getBarCode(String msg,String path) {
        String imgPath = "";
        try {
            File outFile = new File(fileDir);
            File file = new File(outFile.getAbsoluteFile()+path);
            if(!file.getParentFile().exists()){ //判断文件父目录是否存在
                file.getParentFile().mkdirs();
            }
            imgPath = outFile.getAbsoluteFile()+path;
            OutputStream ous = new FileOutputStream(file);
            if (StringUtils.isEmpty(msg) || ous == null)
                return null;
            //选择条形码类型(好多类型可供选择)
            Code128Bean bean = new Code128Bean();
            //设置长宽
            final double moduleWidth = 0.20;
            final int resolution = 150;
            bean.setModuleWidth(moduleWidth);
            bean.doQuietZone(false);
            String format = "image/png";
            // 输出流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format,
                    resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            //生成条码
            bean.generateBarcode(canvas, msg);
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imgPath;
    }

    /**
     * 生成二维码
     * @param msg
     * @param path
     */
    public void getCore(String msg,String path){
        try {
            File file=new File(path);
            OutputStream ous=new FileOutputStream(file);
            if(StringUtils.isEmpty(msg) || ous==null)
                return;
            String format = "jpg";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType,String> map =new HashMap<EncodeHintType, String>();
            //设置编码 EncodeHintType类中可以设置MAX_SIZE， ERROR_CORRECTION，CHARACTER_SET，DATA_MATRIX_SHAPE，AZTEC_LAYERS等参数
            map.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            map.put(EncodeHintType.MARGIN,"2");
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(msg, BarcodeFormat.QR_CODE,200,200,map);
            MatrixToImageWriter.writeToStream(bitMatrix,format,ous);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
