package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustEntryMapper {
//    StocktakeItemDTOC getItemInfo(@Param("itemCode") String itemCode, @Param("piCd") String piCd, @Param("piDate") String piDate);
    StocktakeItemDTOC getItemInfo(@Param("itemCode")String itemCode,@Param("businessDate")String businessDate
                                 ,@Param("storeCd")String storeCd);

    int getCountByPicd(@Param("piCd") String piCd);

    void deleteByPicd(@Param("piCd") String piCd);

    void save(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);

    /**
     * 获取费用商品明细
     */
    List<StocktakeItemDTOC> getPI0130ByPrimary(@Param("piCd") String piCd,@Param("storeCd")String storeCd);

    void saveItem(@Param("param") PI0100DTOC item);

    void saveAllItem(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);

    void updateItem(@Param("param")PI0100DTOC item);

    List<StocktakeItemDTOC> getPI0130ByPrimaryIn(@Param("piCd") String piCd);

    void saveAllItemIn(@Param("list") List<StocktakeItemDTOC> stocktakeItemList);

    List<StocktakeItemDTOC> getPI0130ByPrimaryIIn(@Param("piCd")String piCd, @Param("articleId")String articleId,@Param("barcode")String barcode);
}
