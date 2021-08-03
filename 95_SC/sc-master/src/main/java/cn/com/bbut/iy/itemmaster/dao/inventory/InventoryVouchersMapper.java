package cn.com.bbut.iy.itemmaster.dao.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.dto.ma8350.MA8350dto;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存传票一览DAO
 *
 */
@Mapper
@Component
public interface InventoryVouchersMapper {

        /**
         * 库存一览条件查询
         */
        List<InventoryVouchersGridDTO> selectListByCondition(InventoryVouchersParamDTO param);

        /**
         * 库存一览条件查询记录总数
         */
        int selectCountByCondition(InventoryVouchersParamDTO param);

        /**
         * 店间转出类型&条件查询
         */
        List<InventoryVouchersGridDTO> selectByTypeCondition(InventoryVouchersParamDTO param);

        /**
         * 类型&条件查询记录总数
         */
        int selectCountByTypeCondition(InventoryVouchersParamDTO param);

        /**
         * 店间转入查询数据
         */
        List<InventoryVouchersGridDTO> selectByTypeInCondition(InventoryVouchersParamDTO param);

        /**
         * 店间转入查询数据记录总数
         */
        int selectCountByTypeInCondition(InventoryVouchersParamDTO param);

        /**
         * 查询商品信息
         */
        ItemInfoDTO selectItemInfoByCode(String storeCd, String itemCode, String businessDate);

        /**
         * 查询店铺信息
         */
        StoreInfoDTO selectStoreByCode(String storeCd, String businessDate);

        /**
         * 保存头档信息
         */
        int insertSk0010(Sk0010DTO sk0010);

        /**
         * 修改头档信息
         * @param sk0010
         * @return
         */
        int updateSk0010(Sk0010DTO sk0010);

        /**
         * 保存明细信息
         */
        int insertSk0020(Sk0020DTO sk0020);

        int updateActualQty(String voucherNo,String storeCd,BigDecimal actualQty,String articleId);

        /**
         * 删除明细信息
         */
        int deleteSk0020ByKey(Sk0020DTO sk0020);

        /**
         * 更新总金额
         */
        int updateSk0010Amt(Sk0020DTO sk0020);

        /**
         * 查询传票商品详情
         */
        List<Sk0020DTO> selectListSk0020(Sk0020ParamDTO sk0020);
        List<Sk0020DTO> selectListSk0020OutStore(Sk0020ParamDTO sk0020);

        /**
         * 查询转出商品详情
         */
        List<Sk0020DTO> selectDetailSk0020(Sk0020ParamDTO sk0020);

        /**
         * store 下拉列表
         */
        List<AutoCompleteDTO> getStoreList(@Param("v") String v,
@Param("businessDate") String businessDate);

        /**
         * item 下拉列表
         */
        List<AutoCompleteDTO> getItemList(@Param("storeCd") String storeCd,
        @Param("v") String v,
         @Param("businessDate") String businessDate);

        List<AutoCompleteDTO> getInventoryItemList(@Param("storeCd") String storeCd,
                                          @Param("v") String v,
                                          @Param("businessDate") String businessDate);
        /**
         * item 转出单号下拉列表
         */
        List<AutoCompleteDTO> getDOCList(@Param("storeCd") String storeCd,
@Param("storeCd1") String storeCd1,
@Param("v") String v);

        /**
         * 判断是否多次转移
         */
        List<InventoryVouchersGridDTO> existsDOC(@Param("storeCd") String storeCd,
@Param("storeCd1") String storeCd1);

        /**
         * 判断转出单号是否审核通过
         */
        List<InventoryVouchersGridDTO> getApprovedList(@Param("storeCd") String storeCd,
@Param("storeCd1") String storeCd1);

        /**
         * 更新转出货物的实际转入数量
         */
        int updateQty1(@Param("dto") Sk0020DTO sk0020DTO);

        /**
         * 同步调拨商品数量不一致的原因
         */
        int updateDifferenceReasons(@Param("dto") Sk0020DTO sk0020DTO);

        /**
         * 实时库存量查询
         */
        String  selectStock(@Param("storeCd") String storeCd,
@Param("itemId") String itemId);

        /**
         * 店内转移条件查询
         * @param param
         * @return
         */
        List<InventoryVouchersGridDTO> selectItemTransfer(InventoryVouchersParamDTO param);

        /**
         * 店内转移查询记录总数
         */
        int selectCountItemTransfer(InventoryVouchersParamDTO param);

        /**
         * 查询店内转移明细数据
         * @param sk0020
         * @return
         */
        List<Sk0020DTO> InstoreListSk0020(Sk0020ParamDTO sk0020);

        /**
         * 获取一般水平的理由
         * @param v
         * @return
         */
        List<AutoCompleteDTO> getMa8350(@Param("v")String v);

        /**
         *获取详细的理由
         * @param v
         * @return
         */
        List<AutoCompleteDTO> getMa8360(@Param("v")String v,@Param("num")int num);

        /**
         * 查询库存调整商品详情
         */
        List<Sk0020DTO> adjustDetailsList(Sk0020ParamDTO sk0020);
        Integer adjustItemSKU(Sk0020ParamDTO sk0020);

        int selectSumArticle(Sk0020ParamDTO sk0020);

        List<AutoCompleteDTO> getReasondetailAndGeneralReason(@Param("v") String v);

        MA8350dto getGeneralReasonDetail(@Param("generalLevelCd") String generalLevelCd);

        MA8350dto getNewGeneralReasonDetail(@Param("detailedLevelCd") String detailedLevelCd);

        Ma1000DTO getSouthOrNouth(@Param("storeCd") String storeCd);

        List<AutoCompleteDTO> getOutStoreList(@Param("v") String v,@Param("zoCd") String zoCd,@Param("businessDate") String businessDate);
        List<AutoCompleteDTO> getStoreListByInStore(@Param("v") String v,@Param("vstore") String vstore,@Param("businessDate") String businessDate);

        // 获取上次店间修正的修正单号
        String getLastCorrTransfer(@Param("voucherNo1")String voucherNo1,@Param("voucherType")String voucherType);

        List<Sk0020DTO> getLastCorrQty(String voucherNo);
        List<AutoCompleteDTO> getMa1172OutItemList(@Param("storeCd") String storeCd,
                                          @Param("v") String v,
                                          @Param("businessDate") String businessDate);

        List<AutoCompleteDTO> getMa1172InItemList(@Param("outArticleId")String outArticleId,@Param("storeCd")String storeCd, @Param("v") String v,  @Param("businessDate") String businessDate);
}
