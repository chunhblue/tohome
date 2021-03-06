package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.fsInventory.StocktakeItemDTOD;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0110DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.StocktakeItemDTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface materialEntryMapper {
//    List<StocktakeItemDTOC> getPI0140ByPrimary(@Param("piCd") String piCd,@Param("storeCd") String storeCd);
    List<StocktakeItemDTOC> getPI0140ByPrimary(@Param("piCd") String piCd);

    StocktakeItemDTOC getItemInfo(@Param("itemCode")String itemCode,@Param("businessDate")String businessDate
            ,@Param("storeCd")String storeCd);

    int getCountByPicd(@Param("piCd") String piCd, @Param("piDate") String piDate);

    void deleteByPicd(@Param("piCd") String piCd);

    void saveItem(@Param("param") PI0100DTOC item);

    void saveAllItem(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);
    void saveAllItemIn(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);

    void updateItem(@Param("param")PI0100DTOC item);

    List<StocktakeItemDTOC> getPI0140ByPrimaryIn(@Param("piCd")String piCd,@Param("articleId") String articleId);
}
