package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface StocktakePlanMapper {

    /**
     * 保存盘点计划主档表
     * @param pi0100
     */
    void savePI0100(@Param("pi0100") PI0100DTO pi0100);

    /**
     * 保存盘点计划明细表
     * @param pi0110List
     */
    void savePI0110(@Param("pi0110List") List<PI0110DTO> pi0110List);

    /**
     * 查询数据
     * @param pi0100Param
     * @return
     */
    List<PI0100DTO> search(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    /**
     * 查询记录数
     * @param pi0100Param
     * @return
     */
    int selectCountByParam(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    /**
     * 获得pmaList数据
     * @return
     * @param limit
     * @param page
     * @param rows
     */
    List<PI0110DTO> getPmaList(@Param("limit")int limit, @Param("page")int page, @Param("rows")int rows);

    PI0100DTO getPI0100ByPrimary(@Param("piCd") String piCd, @Param("piDate") String piDate);

    List<PI0110DTO> getPI0110ByPrimary(@Param("piCd") String piCd, @Param("piDate") String piDate);

    List<Map<String, String>> getStocktakeParam(@Param("type") String type);

    void updatePI0100(@Param("pi0100") PI0100DTO pi0100);

    void deletePI0110(@Param("piCd") String piCd, @Param("piDate") String piDate);

    int checkPicd(@Param("piCd") String piCd);

    List<StocktakeItemDTO> queryExport(@Param("piCd") String piCd, @Param("piDate") String piDate, @Param("storeCd")String storeCd);

    // 查询打印数据
    List<PI0100DTO> getPrintData(@Param("pi0100Param") PI0100ParamDTO pi0100Param);

    // 获得 门店下所有商品的分类
    List<PI0110DTO> getAllItemDepartmentByStore(@Param("storeCd")String storeCd, @Param("businessDate")String businessDate);

    // 根据 主键获取盘点差异报表里的数据
    int getPi0125CountByPicd(@Param("piCd")String piCd, @Param("piDate")String piDate, @Param("storeCd")String storeCd);

    // 根据主键删除 盘点差异报表里的数据
    void deletePi0125ByPicd(@Param("piCd")String piCd, @Param("piDate")String piDate, @Param("storeCd")String storeCd);

    // 保存数据到盘点差异表里
    void insertExportItemsToDB(@Param("piCd")String piCd,
                             @Param("piDate")String piDate,
                             @Param("storeCd")String storeCd,
                             @Param("list") List<StocktakeItemDTO> newList);

    // 修改主档的导出时间
    void modifyPI0100ExportTime(@Param("piCd")String piCd,
                                @Param("piDate")String piDate,
                                @Param("storeCd")String storeCd,
                                @Param("exportTime")String exportTime);

    // 修改主档的导出时间
    void modifyPI0100StartTime(@Param("piCd")String piCd,
                                @Param("piDate")String piDate,
                                @Param("storeCd")String storeCd,
                                @Param("startTime")String startTime);

    // 修改主档的盘点结束时间
    void modifyPI0100EndTime(@Param("piCd") String piCd,
                                     @Param("piDate") String piDate,
                                     @Param("storeCd") String storeCd,
                                     @Param("endTime") String endTime) ;



    /**
     * 设置 超过 End Time 没有 submit 提交的 计划过期
     * @param bsDate
     */
    void updateStocktakingPlanExpired(@Param("bsDate") String bsDate);

    /**
     * 添加审核数据
     * @param piCd
     * @return
     */
    int insertAudit(@Param("piCd")String piCd);

    int updateAudit(@Param("pi0100")PI0100DTO pi0100);

    List<Integer> getDayOfEnd(String piDate,String storeCd);

    /**
     * 获取时间段内库存异动的数据
     */
    List<Map<String, Object>> getInventoryData(@Param("param") PI0100ParamDTO param, @Param("tableName")String tableName);

    void createTempTable(@Param("tableName") String tempTableName);

    void saveToTempTable(@Param("tableName")String tempTableName, @Param("list") List<StocktakeItemDTO> list);

    List<StocktakeItemDTO> getstockList(@Param("tableName")String tempTableName);

    /**
     * 将数据存入临时表
     */
    void insertInventoryToTemp(@Param("param")PI0100ParamDTO param, @Param("tableName")String tableName);
}
