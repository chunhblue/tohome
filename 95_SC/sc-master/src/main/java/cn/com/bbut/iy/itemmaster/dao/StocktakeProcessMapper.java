package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StocktakeProcessMapper {
    /**
     * 查询数据
     * @param pi0100Param
     * @return
     */
    List<StocktakeProcessDTO> search(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    int searchCount(@Param("pi0100Param") PI0100ParamDTO pi0100);

    // 获得正常盘点的商品
    List<StocktakeProcessItemsDTO> getTableData1(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                 @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                                                 @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                                                 @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                                                 @Param("endAmt") Integer endAmt, @Param("sidx")String sidx, @Param("sord")String sord,
                                                 @Param("page") int page, @Param("rows") int rows, @Param("limit") int limit);

    // 获得为盘到的商品
    List<StocktakeProcessItemsDTO> getTableData2(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                 @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                                                 @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                                                 @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                                                 @Param("endAmt") Integer endAmt, @Param("sidx")String sidx, @Param("sord")String sord,
                                                 @Param("page") int page, @Param("rows") int rows, @Param("limit") int limit);

    // 获得账面上无库存的商品
    List<StocktakeProcessItemsDTO> getTableData3(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                 @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                                                 @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                                                 @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                                                 @Param("endAmt") Integer endAmt, @Param("sidx")String sidx, @Param("sord")String sord,
                                                 @Param("page") int page, @Param("rows") int rows, @Param("limit") int limit);

    List<StocktakeProcessItemsDTO> getTableData4(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                 @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                                                 @Param("page") int page, @Param("rows") int rows, @Param("limit") int limit);

    int getTableData3Count(@Param("piCd") String piCd, @Param("piDate") String piDate,
                           @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                           @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                           @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                           @Param("endAmt") Integer endAmt);

    int getTableData2Count(@Param("piCd") String piCd, @Param("piDate") String piDate,
                           @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                           @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                           @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                           @Param("endAmt") Integer endAmt);

    int getTableData1Count(@Param("piCd") String piCd, @Param("piDate") String piDate,
                           @Param("storeCd") String storeCd, @Param("searchVal") String searchVal,
                           @Param("businessDate") String businessDate, @Param("startQty") Integer startQty,
                           @Param("endQty") Integer endQty, @Param("startAmt") Integer startAmt,
                           @Param("endAmt") Integer endAmt);

    int getTableData4Count(@Param("piCd") String piCd, @Param("piDate") String piDate,
                      @Param("storeCd") String storeCd, @Param("searchVal") String searchVal);

    StocktakeReportDTO getStocktakingHead(@Param("piCd") String piCd, @Param("piDate") String piDate,@Param("storeCd") String storeCd);

    StocktakeReportDTO getStocktakeVariance(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                            @Param("storeCd") String storeCd, @Param("flg") String flg, @Param("depCd") String depCd);

    Integer _getStocktakePeriodSaleAmt(@Param("piDate") String piDate,
                                      @Param("storeCd") String storeCd, @Param("startTime")String startTime, @Param("endTime")String endTime);

    Integer getLastToThisSaleAmt(@Param("param") StocktakeReportDTO headInfo);

    List<StocktakeReportItemDTO> getStocktakeReportItems(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                         @Param("storeCd") String storeCd);


    StocktakeReportItemDTO getGrandTotal(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                         @Param("storeCd") String storeCd);

    // 根据分类获得 差异数据
    List<StocktakeReportVarianceDTO> getVarianceByCategory(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                           @Param("storeCd") String storeCd, @Param("businessDate")String businessDate,
                                                           @Param("flg") String flg);

    // 获取过去六次的盘点数据
    List<StocktakeReportByDepDTO> getVarianceByDep(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                                 @Param("storeCd") String storeCd);

    // 获得过去六次盘点CD
    List<StocktakeProcessDTO> getPastPiCd(@Param("piCd") String piCd, @Param("piDate") String piDate,
                             @Param("storeCd") String storeCd);

    int getBadQty(@Param("piCd") String piCd, @Param("piDate") String piDate,
                  @Param("storeCd") String storeCd);

    List<StocktakeProcessItemsDTO> getBadMerchandisingList(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                           @Param("storeCd") String storeCd, @Param("businessDate")String businessDate);


    List<StocktakeProcessItemsDTO> getExceptionItemList(@Param("piCd") String piCd, @Param("piDate") String piDate,
                                                           @Param("storeCd") String storeCd);
}
