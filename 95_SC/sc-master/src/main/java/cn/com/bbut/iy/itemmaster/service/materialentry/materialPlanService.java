package cn.com.bbut.iy.itemmaster.service.materialentry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100ParamDTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0110DTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface materialPlanService {
    List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session);
}
