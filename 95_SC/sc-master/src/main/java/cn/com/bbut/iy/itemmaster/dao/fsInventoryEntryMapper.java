package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.fsInventory.StocktakeItemDTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface fsInventoryEntryMapper {
    List<StocktakeItemDTOC> getPI0140ByPrimary(@Param("piCd") String piCd,@Param("storeCd")String storeCd);

    StocktakeItemDTOC getItemInfo(@Param("itemCode") String itemCode,@Param("businessDate")String businessDate,@Param("storeCd")String storeCd);

    int getCountByPicd(@Param("piCd") String piCd);

    void deleteByPicd(@Param("piCd") String piCd);

    void saveItem(@Param("param") PI0100DTOC item);

    void saveAllItem(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);

    void updateItem(@Param("param")PI0100DTOC item);

}
