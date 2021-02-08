package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SuspendSaleMapper {

    // 保存
    int insertSuppendSaleItem(SA0070 record);

    String getDocId(@Param("docId") String docId);

    List<PriceDetailGridDto> getList(PriceDetailParamDto param);

    long getListCount(PriceDetailParamDto param);

    // List<AutoCompleteDTO> selectListByLevel(@Param("level") String level, @Param("adminId")String adminId);

    List<AutoCompleteDTO> selectListByLevel0(@Param("level") String level, @Param("adminId") String adminId,
                                             @Param("articleId") String articleId,@Param("accDate")String accDate,
                                             @Param("v")String v);

    List<AutoCompleteDTO> selectListByLevel1(@Param("level") String level, @Param("adminId") String adminId,
                                             @Param("articleId") String articleId,@Param("accDate")String accDate,
                                             @Param("v")String v);

    List<AutoCompleteDTO> selectListByLevel2(@Param("level") String level, @Param("adminId") String adminId,
                                             @Param("articleId") String articleId,@Param("accDate")String accDate,
                                             @Param("v")String v);

    List<AutoCompleteDTO> selectListByLevel3(@Param("level") String level, @Param("adminId") String adminId,
                                             @Param("articleId") String articleId,@Param("accDate")String accDate,
                                             @Param("v")String v);

    List<AutoCompleteDTO> getStoreListByDistrictCd(@Param("cityCd")String cityCd,@Param("districtCd") String districtCd,
                                                   @Param("articleId")String articleId, @Param("accDate")String accDate);

}
