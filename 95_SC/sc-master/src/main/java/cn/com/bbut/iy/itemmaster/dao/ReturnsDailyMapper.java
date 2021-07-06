package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReturnsDailyMapper {

    // 查询退货数据
    List<ReturnsDailyDTO> search(@Param("param") ReturnsDailyParamDTO param);

    List<ReturnsDailyDTO> searchNoSale(@Param("param") ReturnsDailyParamDTO param);

    ReturnsDailyDTO getItemTotal(@Param("param") ReturnsDailyParamDTO param);

    Integer searchNoSaleCount(@Param("param") ReturnsDailyParamDTO param);

    List<ReturnsDailyDTO> getAllTranTime(@Param("param") ReturnsDailyParamDTO param);

    Ma1000 selectByStoreCd(@Param("storeCd") String storeCd, @Param("businessDate") String businessDate);

    ReturnsDailyDTO getNextTranTime(@Param("storeCd") String storeCd,@Param("posId") String posId,@Param("tranDate")String tranDate);
}
