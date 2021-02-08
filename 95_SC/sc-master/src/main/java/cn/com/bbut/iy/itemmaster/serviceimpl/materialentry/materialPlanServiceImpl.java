package cn.com.bbut.iy.itemmaster.serviceimpl.materialentry;

import cn.com.bbut.iy.itemmaster.dao.fsInventoryPlanMapper;
import cn.com.bbut.iy.itemmaster.dao.materialPlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100ParamDTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0110DTOD;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100ParamDTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0110DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.StocktakeItemDTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.service.fsInvenoryentry.fsInventoryPlanService;
import cn.com.bbut.iy.itemmaster.service.materialentry.materialPlanService;
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
public class materialPlanServiceImpl implements materialPlanService {
    @Autowired
    private materialPlanMapper materialplanMapper;


    @Override
    public List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session) {
        return materialplanMapper.getPmaList();
    }

    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day + "/" + month + "/" + year;
    }

}
