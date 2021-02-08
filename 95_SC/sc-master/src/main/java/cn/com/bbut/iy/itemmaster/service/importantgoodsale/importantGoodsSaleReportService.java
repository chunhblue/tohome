package cn.com.bbut.iy.itemmaster.service.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;

import java.util.List;
import java.util.Map;

public interface importantGoodsSaleReportService {


    List<MA0080> getPamList();

    List<Ma1000> getAMlist();

    List<MA0020C> getClityList();

    Map<String,Object> getGoodSaleReportContent(importantgoodSaleReportParamDTO param);
}
