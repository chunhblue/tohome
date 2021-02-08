package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryParamDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.StoreItemWarehousCk;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface RealTimeInventoryQueryMapper {
    // 条件查询
    List<RTInventoryQueryDTO> InventoryQueryBy(RTInventoryQueryParamDTO rTParamDTO);

    // 查询总条数
    long selectCountByCondition(RTInventoryQueryParamDTO rTParamDTO);

    // 查询库存其他信息
    List<RTInventoryQueryDTO> InventoryEsQuery(RTInventoryQueryParamDTO rTParamDTO);

    // 获取商品的大中小分类
    List<SaveInventoryQty> getMoreInformation(Collection<String> articles, @Param("businessDate")String businessDate);

    // 获取该商品在BI的所有warehous_date时间下的库存信息
    List<SaveInventoryQty> getBIDate(String storeCd,Collection<String> articles);
    // 获去BI的实时库存信息
    SaveInventoryQty getBIDetailInfo(String storeCd,String articleId,String warehousDate);

    // 删除BI库存
    int deleteBiInventory(String storeCd, Collection<String> deleteAllList);

    int updateBiInventory(String storeCd, String articleId, String warehousDate, BigDecimal gapsQty,BigDecimal gapsAmt);

    // 取得商品的平均价格
    List<SaveInventoryQty> selectAvgCost(String storeCd, Collection<String> articles,String businessDate);

    int addBitemWarehousCk(Collection<StoreItemWarehousCk> list);

    int addRtQtyListToEs(Collection<SaveInventoryQty> list);
}
