package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VendorReturnedDailyMapper {
    List<VendorReturnedDailyDTO> search(@Param("param")VendorReturnedDailyParamDTO param);

    int searchCount(@Param("param")VendorReturnedDailyParamDTO param);

    List<AutoCompleteDTO> getAMList(@Param("flag")String flag, @Param("v")String v);

    List<AutoCompleteDTO> getOMList(@Param("flag")String flag, @Param("v")String v);

    List<VendorReturnedDailyDTO> dcDailySearch(@Param("param")VendorReturnedDailyParamDTO param);

    int dcDailySearchCount(@Param("param")VendorReturnedDailyParamDTO param);
}