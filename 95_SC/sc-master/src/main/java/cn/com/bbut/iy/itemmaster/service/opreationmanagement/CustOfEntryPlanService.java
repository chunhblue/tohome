package cn.com.bbut.iy.itemmaster.service.opreationmanagement;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CustOfEntryPlanService {
    // 保存
    int insert(PI0100DTOC pi0100, List<PI0110DTOC> list);

    // 查询数据
    GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param);

    List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session);

    PI0100DTOC getData(String piCd,String storeCd);

    Map<String,List<Map<String,String>>> getComboxData();

    int update(PI0100DTOC pi0100c, List<PI0110DTOC> pi0110List);

    int checkPicd(String piCd);

    HSSFWorkbook getExportHSSFWorkbook(PI0100ParamDTOC pi0100Paramc, String formCondition, String piCd, String piDate);

    List<PI0100DTOC> getPrintData(PI0100ParamDTOC pi0100Param);
}
