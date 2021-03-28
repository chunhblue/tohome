package cn.com.bbut.iy.itemmaster.dao.clearInventory;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryParamDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClearInventoryMapper {

    /**
     * 添加数据进临时表
     */
    void insertTempTable(@Param("tempTableName") String tempTableName, @Param("list") List<ClearInventoryDTO> list);

    /**
     * 利用临时表补充在库量
     */
    List<ClearInventoryDTO> getItemInfo(@Param("tempTableName")String tempTableName, @Param("businessDate")String businessDate);

    /**
     * 保存数据
     * @param list
     */
    void insertToDB(@Param("list") List<ClearInventoryDTO> list);

    void deleteByKey(@Param("list") List<ClearInventoryDTO> list, @Param("businessDate")String businessDate);

    /**
     * 查询总记录
     */
    int searchCount(@Param("param") ClearInventoryParamDTO param, @Param("businessDate")String businessDate);

    /**
     * 查询记录
     */
    List<ClearInventoryDTO> search(@Param("param") ClearInventoryParamDTO param, @Param("businessDate")String businessDate);

    /**
     * 获取商品List
     */
    List<AutoCompleteDTO> getItemList(@Param("v") String v, @Param("bsDate") String bsDate);

    /**
     * 获得商品得详细信息
     */
    ClearInventoryDTO getArticle(@Param("articleId")String articleId, @Param("bsDate") String bsDate);

    /**
     * insert
     * @param dto
     */
    void insert(@Param("param") ClearInventoryDTO dto);
}
