package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.article.ArticleDTO;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StocktakeEntryMapper {

    StocktakeItemDTO getItemInfo(@Param("itemCode") String itemCode, @Param("piCd") String piCd, @Param("piDate") String piDate);

    int getCountByPicd(@Param("piCd") String piCd, @Param("piDate") String piDate);

    void deleteByPicd(@Param("piCd") String piCd, @Param("piDate") String piDate);

    void save(@Param("list") List<StocktakeItemDTO> stocktakeItemList);

    List<StocktakeItemDTO> getPI0120ByPrimary(@Param("piCd") String piCd, @Param("piDate") String piDate);
    List<StocktakeItemDTO> getPI0120tBy(@Param("piCd") String piCd, @Param("piDate") String piDate);

    void updateMainStatus(@Param("piCd") String piCd, @Param("piDate") String piDate, @Param("status")String status);

    List<StocktakeItemDTO> getItemInfoByList(@Param("idList") List<String> idList, @Param("piCd") String piCd, @Param("piDate") String piDate);

    List<AutoCompleteDTO> getItemList(@Param("piCd") String piCd, @Param("piDate")String piDate,@Param("piStoreCd")String piStoreCd, @Param("v")String v);

    List<StocktakeItemDTO> getItemVarianceReport(@Param("piCd") String piCd, @Param("piDate")String piDate,
                                                 @Param("businessDate")String businessDate, @Param("storeCd")String storeCd);

    void updateStocktakingVarianceReport(@Param("list") List<StocktakeItemDTO> newList);

    void updatePi0125(@Param("piCd") String piCd, @Param("piDate")String piDate, @Param("storeCd")String storeCd);

    int selectCountByParam(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    List<PI0100DTO> search(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    // 创建临时表
    void createTempTable(@Param("tableName") String tempTableName);
    // 创建Txt临时表
    void createTxtTempTable(@Param("tableName") String tempTableName);

    // 保存数据到临时表
    void saveToTempTable(@Param("tableName")String tempTableName, @Param("list") List<StocktakeItemDTO> list);
    // 保存Txt数据到临时表
    void saveToTxtTempTable(@Param("tableName")String tempTableName, @Param("list") List<StocktakeItemDTO> list);

    // 从临时表中补全信息
    List<StocktakeItemDTO> getTempItemList(@Param("tableName")String tempTableName, @Param("businessDate") String businessDate,@Param("storeCd")String storeCd);
    // 从Txt临时表中补全信息
    List<StocktakeItemDTO> getTempTxtItemList(@Param("tableName")String tempTableName, @Param("businessDate") String businessDate);

    int countOldItem(@Param("articleId")String articleId,@Param("businessDate")String businessDate);
    // 删除临时表
    void deleteTempTable(@Param("tableName")String tempTableName);

    List<MA1100> getItemInformation(@Param("articles")Collection<String> articles,String businessDate);

    void deleteItem();
    // 保存原材料商品信息到DB
    int insertNonCountListToDb(@Param("nonCountList")Collection<MA1100> nonCountList);

    List<ArticleDTO> getRawItemList(ArticleParamDTO dto);

    int countRawItemList(ArticleParamDTO dto);

    List<StocktakeItemDTO> getTempExceptionItemList(@Param("tableName")String exceptionTableName,@Param("list")Collection<String> items);

    void tempToExcepition(@Param("list") List<StocktakeItemDTO> exceptionItemList,
                          @Param("piCd")String piCd,@Param("piDate")String piDate,
                          @Param("storeCd")String storeCd);

    int getCountTable(@Param("tableName")String exceptionTableName);

    List<String> getArticles(@Param("list")Collection<String> items);


    void deleteExByPicd(@Param("piCd") String piCd, @Param("piDate") String piDate);
    void deleteExMore(@Param("piCd") String piCd, @Param("piDate") String piDate,@Param("list")Collection<String> items);

    PI0100DTO getPi0100Info(String storeCd,String piCd);
}
