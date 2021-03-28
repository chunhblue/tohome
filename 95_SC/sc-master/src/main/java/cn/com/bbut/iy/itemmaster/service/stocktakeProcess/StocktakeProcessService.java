package cn.com.bbut.iy.itemmaster.service.stocktakeProcess;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * @author lz
 */
public interface StocktakeProcessService {

    GridDataDTO<StocktakeProcessDTO> search(PI0100ParamDTO pi0100);

    PI0100DTO getData(String piCd, String piDate);

    GridDataDTO<StocktakeProcessItemsDTO> getTableData1(String piCd, String piDate,
                                                        String storeCd, String searchVal,
                                                        Integer startQty, Integer endQty,
                                                        Integer startAmt, Integer endAmt,
                                                        String sidx, String sord,
                                                        int page, int rows);

    GridDataDTO<StocktakeProcessItemsDTO> getTableData2(String piCd, String piDate,
                                                        String storeCd, String searchVal,
                                                        Integer startQty, Integer endQty,
                                                        Integer startAmt, Integer endAmt,
                                                        String sidx, String sord,
                                                        int page, int rows);

    GridDataDTO<StocktakeProcessItemsDTO> getTableData3(String piCd, String piDate,
                                                        String storeCd, String searchVal,
                                                        Integer startQty, Integer endQty,
                                                        Integer startAmt, Integer endAmt,
                                                        String sidx, String sord,
                                                        int page, int rows);

    GridDataDTO<StocktakeProcessItemsDTO> getTableData4(String piCd, String piDate,
                                                        String storeCd, String searchVal,int page, int rows);

    List<StocktakeProcessDTO> getPrintData(PI0100ParamDTO pi0100);

    /**
     * 生成 excel
     */
    SXSSFWorkbook getExportHSSFWorkbook(String piCd, String piDate, String storeCd);
}
