package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.OP0060Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060GridDto;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060ParamDto;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.op0060.OP0060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.OP0060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


@Service
@Slf4j
public class OP0060ServiceImpl implements OP0060Service {
    @Autowired
    private OP0060Mapper op0060Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Value("${file.fileDir}")
    private String fileDir;

    @Override
    public GridDataDTO<OP0060GridDto> getList(OP0060ParamDto param) {
        String businessDate = cm9060Service.getValByKey("0000");
        param.setBusinessDate(businessDate);
        List<OP0060GridDto> list = op0060Mapper.getList(param);
        long count = op0060Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    @Transactional
    public int insertOrUpdate(OP0060 op0060, String flg) {
        int optionFlg = 0;
        if("add".equals(flg)){
            String deltaNo = sequenceService.getSequence("op0060_delta_no_seq");
            if(StringUtils.isBlank(deltaNo)){
                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "op0060_delta_no_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ op0060_delta_no_seq ]");
                throw e;
            }
            op0060.setDeltaNo(Integer.parseInt(deltaNo));
            optionFlg = op0060Mapper.insertSelective(op0060);
        }else if ("update".equals(flg)){
            //删除附件信息
            MA4320Example example = new MA4320Example();
            example.or().andInformCdEqualTo(op0060.getDeltaNo().toString()).andFileTypeEqualTo("05");
            ma4320Mapper.deleteByExample(example);
            optionFlg = op0060Mapper.updateByPrimaryKeySelective(op0060);
        }
        //添加附件信息
        if(StringUtils.isNotBlank(op0060.getFileDetailJson())){
            List<MA4320> ma4320List = new Gson().fromJson(op0060.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
            for (int j = 0; j < ma4320List.size(); j++) {
                MA4320 ma4320 = ma4320List.get(j);
                ma4320.setInformCd(op0060.getDeltaNo().toString());
                ma4320.setCreateUserId(op0060.getCreateUserId());
                ma4320.setCreateYmd(op0060.getCreateYmd());
                ma4320.setCreateHms(op0060.getCreateHms());
                ma4320Mapper.insertSelective(ma4320);
            }
        }
        return optionFlg;
    }

    @Override
    @Transactional
    public int deleteByParam(OP0060 op0060) {
        op0060.setRecordSts("00");
        //删除附件信息
        MA4320Example example = new MA4320Example();
        example.or().andInformCdEqualTo(op0060.getDeltaNo().toString()).andFileTypeEqualTo("05");
        ma4320Mapper.deleteByExample(example);
        return op0060Mapper.updateByPrimaryKeySelective(op0060);
    }

    /**
     * 生成pdf表格
     * @param param
     * @return
     */
    @Override
    public List<OP0060GridDto> depositList(OP0060ParamDto param, OutputStream outputStream) {
        String businessDate = cm9060Service.getValByKey("0000");
        param.setBusinessDate(businessDate);
        param.setFlg(false);
        List<OP0060GridDto> _list = op0060Mapper.getList(param);
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // 页面大小(A4横向)
            Rectangle rectangle = new Rectangle(new RectangleReadOnly(842F, 595F));
            // 页面背景颜色
            rectangle.setBackgroundColor(BaseColor.WHITE);
            document.setPageSize(rectangle);
            // 页边距 左，右，上，下
            document.setMargins(5, 5, 5, 5);
            document.open();
            //设置字体
            String path = fileDir + "/Calibri.ttf";
            log.info("<<<<<path:"+path);
            BaseFont bf = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(bf,24f,Font.BOLD,BaseColor.BLACK);
            Font subhead = new Font(bf,10f,Font.BOLD,BaseColor.BLACK);
            Font font = new Font(bf,10f,Font.NORMAL,BaseColor.BLACK);

            Paragraph para = new Paragraph((" "), titleFont);
            para.setLeading(10f);
            para.setSpacingAfter(10f);
            document.add(para);
            // 段落
            Paragraph paragraph = new Paragraph(("Bank Deposit"), titleFont);
            paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
            paragraph.setIndentationLeft(12); //设置左缩进
            paragraph.setIndentationRight(12); //设置右缩进
            paragraph.setFirstLineIndent(24); //设置首行缩进
            paragraph.setLeading(20f); //行间距
            paragraph.setSpacingBefore(20f); //设置段落上空白
            paragraph.setSpacingAfter(10f); //设置段落下空白
            document.add(paragraph);

            PdfPTable table = new PdfPTable(13);
            // 设置表格宽度比例为%100
            table.setWidthPercentage(100);
            // 设置表格的宽度
            table.setTotalWidth(800);
            // 也可以每列分别设置宽度
            table.setTotalWidth(new float[] { 40, 65, 70, 70, 70, 70, 70, 70, 60, 60, 65, 75, 50 });
            // 锁住宽度
            table.setLockedWidth(true);
            // 设置表格上面空白宽度
            table.setSpacingBefore(10f);
            // 设置表格下面空白宽度
            table.setSpacingAfter(20f);
            // 表格默认无边框为0
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

            PdfPCell cell3 = new PdfPCell(new Paragraph("Area Manager Code", subhead));
            cell3.setBorderColor(BaseColor.GRAY);
            cell3.setPaddingLeft(0);
            cell3.setFixedHeight(40);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph("Area Manager Name", subhead));
            cell4.setBorderColor(BaseColor.GRAY);
            cell4.setPaddingLeft(0);
            cell4.setFixedHeight(40);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Paragraph("Operation Manager Code", subhead));
            cell5.setBorderColor(BaseColor.GRAY);
            cell5.setPaddingLeft(0);
            cell5.setFixedHeight(40);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Paragraph("Operation Manager Name", subhead));
            cell6.setBorderColor(BaseColor.GRAY);
            cell6.setPaddingLeft(0);
            cell6.setFixedHeight(40);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell6);

            PdfPCell cell7 = new PdfPCell(new Paragraph("Operation Controller Code", subhead));
            cell7.setBorderColor(BaseColor.GRAY);
            cell7.setPaddingLeft(0);
            cell7.setFixedHeight(40);
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell7);

            PdfPCell cell8 = new PdfPCell(new Paragraph("Operation Controller Name", subhead));
            cell8.setBorderColor(BaseColor.GRAY);
            cell8.setPaddingLeft(0);
            cell8.setFixedHeight(40);
            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell8);

            PdfPCell cell9 = new PdfPCell(new Paragraph("Business Date", subhead));
            cell9.setBorderColor(BaseColor.GRAY);
            cell9.setPaddingLeft(0);
            cell9.setFixedHeight(40);
            cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell9);

            PdfPCell cell9_5 = new PdfPCell(new Paragraph("Deposit Date", subhead));
            cell9_5.setBorderColor(BaseColor.GRAY);
            cell9_5.setPaddingLeft(0);
            cell9_5.setFixedHeight(40);
            cell9_5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell9_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell9_5);

            PdfPCell cell10 = new PdfPCell(new Paragraph("Store Manager", subhead));
            cell10.setBorderColor(BaseColor.GRAY);
            cell10.setPaddingLeft(0);
            cell10.setFixedHeight(40);
            cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell10);

            PdfPCell cell11 = new PdfPCell(new Paragraph("Bank Deposit Amount", subhead));
            cell11.setBorderColor(BaseColor.GRAY);
            cell11.setPaddingLeft(0);
            cell11.setFixedHeight(40);
            cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell11);

            PdfPCell cell12 = new PdfPCell(new Paragraph("Remarks", subhead));
            cell12.setBorderColor(BaseColor.GRAY);
            cell12.setPaddingLeft(0);
            cell12.setFixedHeight(40);
            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell12);

            for(int i = 0; i< _list.size();i++){
                cell1 = new PdfPCell(new Paragraph(_list.get(i).getStoreCd(), font));
                cell1.setFixedHeight(40);
                cell1.setBorderColor(BaseColor.GRAY);
                cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell1);

                cell2 = new PdfPCell(new Paragraph(_list.get(i).getStoreName(), font));
                cell2.setFixedHeight(40);
                cell2.setBorderColor(BaseColor.GRAY);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell3 = new PdfPCell(new Paragraph(_list.get(i).getOfc(), font));
                cell3.setFixedHeight(40);
                cell3.setBorderColor(BaseColor.GRAY);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell3);

                cell4 = new PdfPCell(new Paragraph(_list.get(i).getOfcName(), font));
                cell4.setFixedHeight(40);
                cell4.setBorderColor(BaseColor.GRAY);
                cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell4);

                cell5 = new PdfPCell(new Paragraph(_list.get(i).getOm(), font));
                cell5.setFixedHeight(40);
                cell5.setBorderColor(BaseColor.GRAY);
                cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell5);

                cell6 = new PdfPCell(new Paragraph(_list.get(i).getOmName(), font));
                cell6.setFixedHeight(40);
                cell6.setBorderColor(BaseColor.GRAY);
                cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell6);

                cell7 = new PdfPCell(new Paragraph(_list.get(i).getOc(), font));
                cell7.setFixedHeight(40);
                cell7.setBorderColor(BaseColor.GRAY);
                cell7.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell7);

                cell8 = new PdfPCell(new Paragraph(_list.get(i).getOcName(), font));
                cell8.setFixedHeight(40);
                cell8.setBorderColor(BaseColor.GRAY);
                cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell8);

                cell9 = new PdfPCell(new Paragraph(fmtDateToStr(_list.get(i).getAccDate()), font));
                cell9.setFixedHeight(40);
                cell9.setBorderColor(BaseColor.GRAY);
                cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell9);

                cell9_5 = new PdfPCell(new Paragraph(fmtDateToStr(_list.get(i).getDepositDate()), font));
                cell9_5.setFixedHeight(40);
                cell9_5.setBorderColor(BaseColor.GRAY);
                cell9_5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell9_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell9_5);

                cell10 = new PdfPCell(new Paragraph(_list.get(i).getPayPerson(), font));
                cell10.setFixedHeight(40);
                cell10.setBorderColor(BaseColor.GRAY);
                cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell10);

                cell11 = new PdfPCell(new Paragraph(formatNum(_list.get(i).getPayAmt().toString()), font));
                cell11.setFixedHeight(40);
                cell11.setBorderColor(BaseColor.GRAY);
                cell11.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell11);

                cell12 = new PdfPCell(new Paragraph(_list.get(i).getDescription(), font));
                cell12.setFixedHeight(40);
                cell12.setBorderColor(BaseColor.GRAY);
                cell12.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell12);
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer searhSameDayInsert(OP0060ParamDto param) {
        return op0060Mapper.searhSameDayInsert(param);
    }


    /**
     * 四舍五入,不保留小数,千分位
     */
    public static String formatNum(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        int i = new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(i);
        return result;
    }

    /**
     * 字符串截取为时间样式
     *
     * @param date String yyyyMMdd
     * @return String dd/MM/yyyy
     */
    public static String fmtDateToStr(String date) {
        if (StringUtils.isBlank(date) || date.length()!=8) {
            return "";
        }
        return date.substring(6, 8) + "/" + date.substring(4, 6) + "/"
                + date.substring(0, 4);
    }
}
