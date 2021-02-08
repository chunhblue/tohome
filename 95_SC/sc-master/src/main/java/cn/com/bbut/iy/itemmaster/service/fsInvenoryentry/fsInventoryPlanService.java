package cn.com.bbut.iy.itemmaster.service.fsInvenoryentry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100ParamDTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0110DTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface fsInventoryPlanService {

    // 查询数据
//    GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param);

    List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session);

    Map<String, List<Map<String, String>>> getComboxData();

}
