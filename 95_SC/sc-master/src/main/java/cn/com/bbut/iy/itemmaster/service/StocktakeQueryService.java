package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryListDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryParam;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryParamDTO;

/**
 * @author lz
 */
public interface StocktakeQueryService {

    GridDataDTO<StocktakeQueryListDTO> getStocktakeQueryList(StocktakeQueryParamDTO paramDTO);

    GridDataDTO<StocktakeQueryItemsDTO> getDetailsList(StocktakeQueryParamDTO param);

    GridDataDTO<StocktakeQueryListDTO> search(StocktakeQueryParam param);

    GridDataDTO<StocktakeQueryItemsDTO> queryItems(String searchJson, int page, int rows);

    int update(String record);

    int updatePiFirstFinish(String jsonStr);

    int updatePiCommit(String jsonStr);
}
