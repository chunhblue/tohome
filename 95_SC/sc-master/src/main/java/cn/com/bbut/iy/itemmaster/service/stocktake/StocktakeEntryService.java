package cn.com.bbut.iy.itemmaster.service.stocktake;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author lz
 */
public interface StocktakeEntryService {

    StocktakeItemDTO getItemInfo(String itemCode, String piCd, String piDate);

    int insert(String piCd, String piDate, String storeCd, List<StocktakeItemDTO> stocktakeItemList, PI0100DTO pi0100);

    PI0100DTO getData(String piCd, String piDate);

    ReturnDTO updateStatus(String piCd, String piDate, int typeId);

    List<StocktakeItemDTO> getItemInfoByList(List<String> mapList, String piCd, String piDate);

    List<AutoCompleteDTO> getItemList(String piCd, String piDate,String piStoreCd, String v);

    void updateStocktakingVarianceReport(String piCd, String piDate, String storeCd, String reviewStatus);

    void updateEndTime(String piCd, String piDate, String storeCd);

    String insertFileUpload(MultipartFile file, HttpServletRequest request, HttpSession session,String storeCd);

    GridDataDTO<PI0100DTO> search(PI0100ParamDTO pi0100Param);

    List<StocktakeItemDTO> insertImportExcel(MultipartFile file,String storeCd);

    // 读取txt文件内容
    Map<String,Object> insertImportTxt(MultipartFile file, String storeCd);
    // MultipartFile -> File
    void inputStreamToFile(InputStream ins, File file);
}
