package cn.com.bbut.iy.itemmaster.dao.goodsalereport;

import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GoodsSaleReportMapper {
    List<MA0080> getPamList();

    List<Ma1000> getAMlist();

    List<MA0020C> getCityList();

     List<goodSaleReportDTO> search(@Param("goodSaleReport") goodSaleReportParamDTO goodSaleReport);

    int searchCount(@Param("goodSaleReport")goodSaleReportParamDTO param);
    int getArticleCount(@Param("goodSaleReport")goodSaleReportParamDTO param);

    BigDecimal getTotalSaleAmount(@Param("goodSaleReport") goodSaleReportParamDTO param);
}
