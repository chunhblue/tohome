package cn.com.bbut.iy.itemmaster.service.stocktake;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface StocktakePlanService {

    // 保存
    String insert(PI0100DTO pi0100, List<PI0110DTO> list);

    // 查询数据
;
    GridDataDTO<PI0100DTO> search(PI0100ParamDTO pi0100Param) throws ParseException;

    GridDataDTO<PI0110DTO> getPmaList(int page, int rows, HttpServletRequest request, HttpSession session);

    PI0100DTO getData(String piCd, String piDate);

    Map<String,List<Map<String,String>>> getComboxData();

    String update(PI0100DTO pi0100, List<PI0110DTO> pi0110List);

    int checkPicd(String piCd);

    HSSFWorkbook getExportHSSFWorkbook(List<StocktakeItemDTO> list);

    File writeToTXTFile(HttpServletRequest request, List<StocktakeItemDTO> list, String piCd);

    List<PI0100DTO> getPrintData(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    ReturnDTO getAllItemDepartmentByStore(String storeCd);

    Future<String> insertExportItemsToDB(String piCd, String piDate, String storeCd, List<StocktakeItemDTO> oldList);

    List<StocktakeItemDTO> queryExport(String piCd,String piDate,String storeCd);

    void updateStocktakingPlanExpired();

    /**
     * 将库存异动数据, 绑定生成excel
     */
    HSSFWorkbook getInventoryHSSFWorkbook(PI0100ParamDTO pi0100Param, String tempTableName);

    // 将数据存入临时表
    void insertInventoryToTemp(PI0100ParamDTO pi0100Param, String tempTableName);
}
