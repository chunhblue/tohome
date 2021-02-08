package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.PI0010GenMapper;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PI0010Mapper extends PI0010GenMapper {

    // 条件查询
    List<StocktakeQueryListDTO> selectStocktakeBy(StocktakeQueryParam stocktakeQueryParam);
    long selectStocktakeByCount(StocktakeQueryParam stocktakeQueryParam);
    //盘点商品查询
    List<StocktakeQueryItemsDTO> selectItemsBy(StocktakeQueryItemParam itemParam);
    long selectItemsByCount(StocktakeQueryItemParam itemParam);
    //盘点商品根据货位查询
    List<StocktakeQueryItemsDTO> selectItemsByAreaCd(StocktakeQueryItemParam item);
    long selectItemsByAreaCdCount(StocktakeQueryItemParam item);

    int searchCount(@Param("stock") StocktakeQueryParam stockParam);

    List<StocktakeQueryListDTO> search(@Param("stock") StocktakeQueryParam stockParam);

    int queryItemCount(@Param("stock")StocktakeQueryParam stockParam);

    List<StocktakeQueryItemsDTO> queryItems(@Param("stock")StocktakeQueryParam stockParam);

    void update(@Param("stock")StocktakeQueryItemsDTO stock);

    void setPiFirstFinish(@Param("stock")StocktakeQueryItemsDTO stock);

    void setPiQtyStatus(@Param("stock")StocktakeQueryItemsDTO stock);

    void setPiCommitFlag(@Param("piCd")String piCd, @Param("accDate")String accDate);

    List<Sk0020DTO> getItemInfoList(@Param("piCd")String piCd, @Param("accDate")String accDate);

    void insertSK0020(@Param("list") List<Sk0020DTO> itemList);

    void insertSK0010(@Param("sk0010") Sk0010DTO sk0010);
}