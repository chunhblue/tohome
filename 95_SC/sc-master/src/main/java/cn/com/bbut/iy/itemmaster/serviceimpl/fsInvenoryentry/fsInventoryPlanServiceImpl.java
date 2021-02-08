package cn.com.bbut.iy.itemmaster.serviceimpl.fsInvenoryentry;

import cn.com.bbut.iy.itemmaster.dao.fsInventoryPlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100ParamDTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0110DTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import cn.com.bbut.iy.itemmaster.service.fsInvenoryentry.fsInventoryPlanService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName fsInventoryPlanServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/23 11:56
 * @Version 1.0
 */
@Service
public class fsInventoryPlanServiceImpl implements fsInventoryPlanService {
   @Autowired
   private fsInventoryPlanMapper  fsInventoryplanMapper;



    @Override
    public Map<String, List<Map<String, String>>> getComboxData() {
        List<Map<String, String>> piType = fsInventoryplanMapper.getfsInventoryParam("00510");
        List<Map<String, String>> piMethod = fsInventoryplanMapper.getfsInventoryParam("00520");
        List<Map<String, String>> piStatus = fsInventoryplanMapper.getfsInventoryParam("00530");
        HashMap<String, List<Map<String, String>>> map = new HashMap<>();
        map.put("piType",piType);
        map.put("piMethod",piMethod);
        map.put("piStatus",piStatus);
        return map;

    }


    @Override
    public List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session) {
        return fsInventoryplanMapper.getPmaList();
    }


    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

}
