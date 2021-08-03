package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.entity.MB1000;
import cn.com.bbut.iy.itemmaster.entity.Mb1700;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.service.ImportExcelService;
import cn.com.bbut.iy.itemmaster.serviceimpl.base.ImportExcelBaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportExcelServiceImpl extends ImportExcelBaseService implements ImportExcelService {
    @Override
    public List<Ma1105> importExcelWithSimple(MultipartFile file, String storeCd, User user, HttpServletRequest req, HttpServletResponse resp) {
        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
            workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到工作表
        Sheet sheet = super.getSheetByWorkbook(workbook, 0);
        if (sheet.getRow(2000) != null){
            throw new RuntimeException("系统已限制单批次导入必须小于或等于2000！");
        }

        realRowCount = sheet.getPhysicalNumberOfRows();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<Ma1105> list = new ArrayList<>();
        Ma1105 ma1105 = null;
        String shelf = "";
        for(Row row:sheet) {
            if(realRowCount == rowNum) {
                break;
            }

            if(super.isBlankRow(row)) {//空行跳过
                continue;
            }

            if(row.getRowNum() == -1) {
                continue;
            }else {
                if(row.getRowNum() == 0) {//第一行表头跳过
                    continue;
                }
            }

            rowNum ++;
            colNum = 0;//从第一列开始
            ma1105 = new Ma1105();
            StringBuilder str = new StringBuilder();
            String isShelf = super.getCellValue(sheet, row, colNum);
            if ((StringUtils.isBlank(isShelf)||!"Shelf".equalsIgnoreCase(isShelf))&&StringUtils.isBlank(shelf)){
                continue;
            }else{
                if("Shelf".equalsIgnoreCase(isShelf)){
                    shelf = super.getCellValue(sheet, row, 3);
                    continue;
                }
            }
            //开始读取数据
            if(StringUtils.isNotBlank(shelf)){
                ma1105.setStoreCd(storeCd);
                ma1105.setCreateDate(sdf.format(new Date()));
                int ido = shelf.indexOf(".");
                if(ido<0){
                    ma1105.setShelf(shelf);
                }else{
                    ma1105.setShelf(shelf.substring(0,ido));
                }
                ma1105.setSubShelf(shelf);
                ma1105.setCreateUserId(user.getUserId());
                ma1105.setDescG(super.getCellValue(sheet, row, 0));
                ma1105.setLoc(super.getCellValue(sheet, row, 1));
                super.validCellValue(sheet, row, 4, "商品Code");
                ma1105.setItemCode(super.getCellValue(sheet, row, 4));
                ma1105.setProductName(super.getCellValue(sheet, row, 6).replace("'","\'"));//商品名称
                ma1105.setVFacing(super.getCellValue(sheet, row, 10));
                ma1105.setHFacing(super.getCellValue(sheet, row, 11));
                ma1105.setDFacing(super.getCellValue(sheet, row, 12));
                ma1105.setTotalFacing(super.getCellValue(sheet, row, 13));
            }
            list.add(ma1105);
        }
        return list;
    }


}
