package cn.com.bbut.iy.itemmaster.serviceimpl.bm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dao.IyBmCkMapper;
import cn.com.bbut.iy.itemmaster.dao.IyBmMapper;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmExcelParam;
import cn.com.bbut.iy.itemmaster.dto.bm.BmJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmOutExcel;
import cn.com.bbut.iy.itemmaster.dto.bm.ItmeForStoreResultDto;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.service.bm.BmService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;

import com.google.gson.Gson;

/**
 * BM
 * 
 * @author songxz
 */
@Slf4j
@Service(value = "bmExcelService")
public class BmExcelServiceImpl implements ExService {
    @Autowired
    private IyRoleService iyRoleService;
    @Autowired
    private IyBmMapper bmMapper;
    @Autowired
    private IyBmCkMapper bmCkMapper;
    @Autowired
    private BmService bmService;
    /** 字符串：9 **/
    private final String STR_NINE = "9";
    /** 正则替换字符串 **/
    private final String REGULAR_TEXT = "\\d{1}";
    /** 字符串：99 **/
    private final String STR_NINES = "99";
    /** excel样式 **/
    private Map<String, CellStyle> MAP_STYLE = new HashMap<>();
    private final String STYPE_KEY_1 = "style_1";
    private final String STYPE_KEY_2 = "style_2";
    private final String STYPE_KEY_3 = "style_3";
    private final String STYPE_KEY_4 = "style_4";
    private final String STYPE_KEY_5 = "style_5";
    private final BigDecimal HUNDRED = new BigDecimal(100);
    /** bm商品状态 3：已生效 */
    private final String BM_STATUS_3 = "3";

    /** bm商品状态 4：手工删除 */
    private final String BM_STATUS_4 = "4";

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        BmExcelParam paramDTO = (BmExcelParam) param;
        Gson gson = new Gson();
        BmJsonParamDTO bmJsonParam = gson.fromJson(paramDTO.getParam().getSearchJson(),
                BmJsonParamDTO.class);
        session.setHeaderListener(new BmExcelHeaderListener(bmJsonParam));
        session.createWorkBook();
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 当前页号
            int page = 1;
            // 每页的数据量
            int rows = 5000;
            // 内容起始下标
            int curRow = 3;
            String quote = Matcher.quoteReplacement(REGULAR_TEXT);
            if (StringUtils.isNotEmpty(bmJsonParam.getDiv())) {
                String temp = bmJsonParam.getDiv().substring(ConstantsDB.COMMON_TWO,
                        ConstantsDB.COMMON_THREE)
                        + STR_NINES;
                bmJsonParam.setDiv(temp.replaceAll(STR_NINE, quote));
            }
            if (StringUtils.isNotEmpty(bmJsonParam.getDpt())) {
                // 画面中dpt实际上是部，2位
                String temp = bmJsonParam.getDpt() + STR_NINE;
                bmJsonParam.setDpt(temp.replaceAll(STR_NINE, quote));
            }

            if (!bmJsonParam.getBmStatus().equals(BM_STATUS_3)
                    && !bmJsonParam.getBmStatus().equals(BM_STATUS_4)) {
                // 全部、新签约、修改、驳回
                createExcelBodyCK(sheet, curRow, bmJsonParam, paramDTO.getPCode(),
                        paramDTO.getRoleIds(), page, rows);
            } else {
                // 已生效 手工删除
                createExcelBody(sheet, curRow, bmJsonParam, paramDTO.getPCode(),
                        paramDTO.getRoleIds(), page, rows);
            }
            File outfile = new File(Utils.getFullRandomFileName());
            session.saveTo(new FileOutputStream(outfile));
            return outfile;
        } finally {
            session.dispose();
        }
    }

    /**
     * 生产excel内容，该方法处理 ck表数据（bm商品状态：全部，新签约，修改，已经驳回）
     * 
     * @param sheet
     * @param curRow
     */
    private void createExcelBodyCK(Sheet sheet, int curRow, BmJsonParamDTO bmJsonParam,
            String pcode, List<Integer> roleIds, int page, int rows) {
        // 数据开始坐标
        int limitStart = getLimitStart(page, rows);
        // 数据结束坐标
        int limitEnd = getLimitEnd(limitStart, rows);
        bmJsonParam.setLimitStart(limitStart);
        bmJsonParam.setLimitEnd(limitEnd);
        List<BmOutExcel> list = getBmOutExcelData(bmJsonParam, pcode, roleIds);
        int no = 1;
        while (list != null && list.size() > 0) {
            for (BmOutExcel ls : list) {
                int curCol = 0;
                Row row = sheet.createRow(curRow);
                Cell cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValueNo(cell, no++);
                //
                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getCreateDpt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getCheckFlg(ls.getCheckFlg()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getNewFlg(ls.getNewFlg()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getUpdateFlg(ls.getUpdateFlg()));

                //
                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getOpFlg(ls.getOpFlg()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getBmType(ls.getBmType()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getBmCode());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getBuyer());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getBuyerName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getStartDate()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getEndDate()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
                setCellValue(cell, ls.getBmDiscountRate());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getBmCount());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getBmPrice());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getStore());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getItemDpt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getItemCode());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getItemName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemPrice());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemPriceDisc());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemGross());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getNumA());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getNumB());

                //
                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getBmItemFlg(ls.getBmItemFlg()));

                //
                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getRejectreason());

                curRow++;
            }
            page++;
            limitStart = getLimitStart(page, rows);
            limitEnd = getLimitEnd(limitStart, rows);
            bmJsonParam.setLimitStart(limitStart);
            bmJsonParam.setLimitEnd(limitEnd);
            list = getBmOutExcelData(bmJsonParam, pcode, roleIds);
        }
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);

        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);

        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);

        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);

        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
    }

    // 得到数据起始下标
    private int getLimitStart(int page, int rows) {
        return (page - 1) * rows;
    }

    // 得到数据结束下标
    private int getLimitEnd(int limitStart, int rows) {
        return limitStart + rows;
    }

    /**
     * 生产excel内容，该方法处理 正式表数据（bm商品状态：已签约、手工删除）
     * 
     * @param sheet
     * @param curRow
     */
    private void createExcelBody(Sheet sheet, int curRow, BmJsonParamDTO bmJsonParam, String pcode,
            List<Integer> roleIds, int page, int rows) {
        // 数据开始坐标
        int limitStart = getLimitStart(page, rows);
        // 数据结束坐标
        int limitEnd = getLimitEnd(limitStart, rows);
        bmJsonParam.setLimitStart(limitStart);
        bmJsonParam.setLimitEnd(limitEnd);
        List<BmOutExcel> list = getBmOutExcelData(bmJsonParam, pcode, roleIds);
        int no = 1;
        while (list != null && list.size() > 0) {

            for (BmOutExcel ls : list) {
                Row row = sheet.createRow(curRow);
                int curCol = 0;
                Cell cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValueNo(cell, no++);

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getCreateDpt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, getBmType(ls.getBmType()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getBmCode());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getStartDate()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getEndDate()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
                setCellValue(cell, ls.getBmDiscountRate());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getBmCount());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getBmPrice());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getStore());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, ls.getItemDpt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getItemCode());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getItemName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemPrice());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemPriceDisc());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getItemGross());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getNumA());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getNumB());

                curRow++;
            }
            page++;
            limitStart = getLimitStart(page, rows);
            limitEnd = getLimitEnd(limitStart, rows);
            bmJsonParam.setLimitStart(limitStart);
            bmJsonParam.setLimitEnd(limitEnd);
            list = getBmOutExcelData(bmJsonParam, pcode, roleIds);
        }
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
    }

    private List<BmOutExcel> getBmOutExcelData(BmJsonParamDTO bmJsonParam, String pCode,
            List<Integer> roleIds) {
        List<IyResourceDTO> paramResourceList = bmService.paramResourceList(roleIds, pCode);
        bmJsonParam.setResources(paramResourceList);
        List<BmOutExcel> restList = null;
        if (!bmJsonParam.getBmStatus().equals(BM_STATUS_3)
                && !bmJsonParam.getBmStatus().equals(BM_STATUS_4)) {
            // CK表数据
            restList = bmCkMapper.getBmOutExcelData(bmJsonParam);
        } else {
            // 正式表数据
            restList = bmMapper.getBmOutExcelData(bmJsonParam);
        }
        if (restList == null || restList.size() == 0) {
            return null;
        }
        for (BmOutExcel ls : restList) {

            // BM折扣率 需要显示折扣 使用 金额换算方式 除100，否则不对了
            ls.setBmDiscountRate(moneyFmt(ls.getBmDiscountRate()));
            // BM销售价格
            ls.setBmPrice(moneyFmt(ls.getBmPrice()));
            // 单品售价
            ls.setItemPrice(moneyFmt(ls.getItemPrice()));
            // 单品折扣售价
            ls.setItemPriceDisc(moneyFmt(ls.getItemPriceDisc()));

            // 拿到商品进售价信息
            ItmeForStoreResultDto itemInfo = bmMapper.getItemStoreInfoByDate(getSelectMap(
                    ls.getItemSystem(), ls.getStartDate(), ls.getEndDate(), ls.getStore()));
            // 进货单价
            BigDecimal costtax = itemInfo.getCostTax();
            // 折扣销售单价
            BigDecimal priceDisc = ls.getItemPriceDisc();
            BigDecimal profitrate = division(priceDisc.subtract(costtax), priceDisc, 5);
            /** ---所有金额需要除100 去取位小数 四舍五入----- **/
            // 毛利率=（折扣销售单价-进货单价）/折扣销售单价（保留二位小数）如毛利率 ≤ 0，显示为红色
            ls.setItemGross(rateFmt(profitrate));

        }
        return restList;
    }

    private Map<String, String> getSelectMap(String itemSystem, String startDate, String endDate,
            String store) {
        Map<String, String> selectMap = new HashMap<>();
        selectMap.put("itemSystem", itemSystem);
        selectMap.put("startDate", startDate);
        selectMap.put("endDate", endDate);
        selectMap.put("store", store);
        return selectMap;
    }

    // 率 格式化 除100，保留2位小数 四舍五入
    private BigDecimal rateFmt(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP);
    }

    // 率 格式化 除100，保留2位小数 四舍五入
    private BigDecimal moneyFmt(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * 生成报表样式
     * 
     * @param wb
     */
    private void createExcelStyleToMap(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName(Constants.DEFAULT_FONT);
        DataFormat df = wb.createDataFormat();// 格式化使用
        MAP_STYLE.clear();

        // 文本居中+边线
        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        MAP_STYLE.put(STYPE_KEY_1, style);

        // 文本左对齐+边线
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_2, style);

        // 数字右对齐，无小数
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("#,###,##0"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_3, style);

        // 价格右对齐，有2位小数
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("#,###,##0.00"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_4, style);

        // 率 右对齐，有2位小数，和%号
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("##0.00%"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_5, style);
    }

    // 字符串赋值
    private void setCellValueNo(Cell cell, Integer value) {
        if (value != null) {
            cell.setCellValue(value.intValue());
        } else {
            cell.setCellValue("");
        }
    }

    // 字符串赋值
    private void setCellValue(Cell cell, String value) {
        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
    }

    // 数字赋值
    private void setCellValue(Cell cell, Integer value) {
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
    }

    // 金额赋值
    private void setCellValue(Cell cell, BigDecimal value) {
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
    }

    // 得到BM商品状态中修改类型的下拉项对应中文翻译
    private String getUpdateFlg(String upType) {
        if (upType == null) {
            return "";
        }
        switch (upType) {
        case "0":
            return "修改BM价格/折扣";
        case "1":
            return "期间延长";
        case "2":
            return "修改价格和期间";
        default:
            return "全部";
        }
    }

    // 得到BM类型的对应中文翻译
    private String getBmType(String bmType) {
        if (bmType == null) {
            return "";
        }
        switch (bmType) {
        case "01":
            return "01 捆绑";
        case "02":
            return "02 混合";
        case "03":
            return "03 固定组合";
        case "04":
            return "04 阶梯折扣";
        case "05":
            return "05 AB组";
        default:
            return "全部";
        }
    }

    // 得到BM所属的对应中文翻译
    private String getBmItemFlg(String flg) {
        if (flg == null) {
            return "";
        }
        switch (flg) {
        case "0":
            return "未确认";
        case "1":
            return "已确认";
        case "2":
            return "驳回";
        default:
            return "系统未知";
        }
    }

    // 得到BM所属的对应中文翻译
    private String getNewFlg(String flg) {
        if (flg == null) {
            return "";
        }
        switch (flg) {
        case "0":
            return "Add";
        case "1":
            return "修改";
        case "2":
            return "删除";
        default:
            return "系统未知";
        }
    }

    // 得到商品状态的对应中文翻译
    private String getCheckFlg(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
        case "0":
            return "所有采购员均已确认，商品部长未审核";
        case "1":
            return "商品部长已审核（通过），系统部未审核";
        case "2":
            return "系统部审核通过";
        case "3":
            return "已驳回";
        case "4":
            return "采购员确认中";
        default:
            return "系统未知";
        }
    }

    // 得到商品状态的对应中文翻译
    private String getOpFlg(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
        case "01":
            return "采购员提交";
        case "07":
            return "采购员确认BM明细单品";
        case "08":
            return "采购员驳回BM明细单品";
        case "17":
            return "商品部长审核通过";
        case "18":
            return "商品部长驳回";
        case "21":
            return "系统部提交";
        case "27":
            return "系统部审核通过";
        case "28":
            return "系统部驳回";
        default:
            return "系统未知";

        }
    }

    // 字符串截取为时间样式
    private String fmtDateToStr(String date) {
        return TimeUtil.getStrDateByIntDate(date);
    }

    private BigDecimal division(Integer val, BigDecimal divisor, int scale) {
        if (val == null) {
            return null;
        }
        return division(new BigDecimal(val), divisor, scale);
    }

    private BigDecimal division(BigDecimal val, BigDecimal divisor, int scale) {
        if (val == null) {
            return null;
        }
        if (divisor.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        return val.divide(divisor, scale, RoundingMode.HALF_UP);
    }
}
