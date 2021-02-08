package cn.com.bbut.iy.itemmaster.dao.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface importantGoodsSaleReportMapper {
    List<MA0080> getPamList();

    List<Ma1000> getAMlist();

    List<MA0020C> getCityList();

     List<importantgoodSaleReportDTO> search(@Param("goodSaleReport") importantgoodSaleReportParamDTO goodSaleReport);

    int searchCount(@Param("goodSaleReport")importantgoodSaleReportParamDTO param);
}
