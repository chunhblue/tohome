package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SA0070GenMapper;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceSts;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SA0070Mapper extends SA0070GenMapper {
    /**
     * 紧急变价一览
     * @param param
     * @return
     */
    List<PriceDetailGridDto> getList(PriceDetailParamDto param);

    long getListCount(PriceDetailParamDto param);

    /**
     * 条码取出货号
     * @param barcode
     * @return
     */
    String getArticleId(@Param("barcode") String barcode);

    /**
     * 货号取出条码
     * @param articleId
     * @return
     */
    String getBarcode(@Param("articleId") String articleId);

    /**
     * 获取变价状态
     * @param articleId
     * @return
     */
    PriceSts getPriceSts(@Param("articleId") String articleId);

    /**
     * 获取变价单号信息
     * @param changeId
     * @return
     */
    String getChangeId(@Param("changeId") String changeId);

    /**
     * 获取变价
     * @param articleId
     * @param effectiveDate
     * @param storeCd
     * @return
     */
    BigDecimal getNewPrice(@Param("articleId") String articleId, @Param("effectiveDate")String effectiveDate, @Param("storeCd")String storeCd);

    /**
     * 获取商品名称
     * @param articleId
     * @param businessDate
     * @return
     */
    String getArticleName(@Param("articleId") String articleId,@Param("businessDate") String businessDate);

    /**
     * 获取商品列表
     * @param articleId
     * @return
     */
    Price getPrice(@Param("articleId") String articleId,@Param("effectiveDate") String effectiveDate);

    String getBarcodeByInfo(@Param("articleId") String articleId,@Param("storeCd") String storeCd,@Param("businessDate") String businessDate);

    List<Price> selectArticleIdAndName(@Param("v")String v, @Param("storeCd") String storeCd, @Param("effectiveDate") String effectiveDate);
}