package cn.com.bbut.iy.itemmaster.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.bbut.iy.itemmaster.dao.gen.IyBmGenMapper;
import cn.com.bbut.iy.itemmaster.dto.bm.BmItemMandCInofDto;
import cn.com.bbut.iy.itemmaster.dto.bm.BmJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmOutExcel;
import cn.com.bbut.iy.itemmaster.dto.bm.ItmeForStoreResultDto;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemM;

public interface IyBmMapper extends IyBmGenMapper {

    /**
     * 得到指定Item Barcode与指定店铺的数量（正式表）
     * 
     * @param map
     *            item，store
     * @return
     */
    long isItemBmExistByItemAndStore(Map<String, String> map);

    /**
     * 得到指定Item Barcode与指定店铺的数量（CK表）
     * 
     * @param map
     *            item，store
     * @return
     */
    long isItemBmCkExistByItemAndStore(Map<String, String> map);

    /**
     * 针对item c表进行检索<br>
     * 查询商品+时间期间 +店铺 是否存在控制记录，存在返回数量
     * 
     * @param selectMap
     * @return
     */
    long verdictItemIndateByMap(Map<String, String> selectMap);

    /**
     * 得到当前指定商品的店铺控制信息
     * 
     * @param selectMap
     * @return
     */
    ItmeForStoreResultDto getItemStoreInfoByDate(Map<String, String> selectMap);

    /**
     * 得到ck表中最大的序列号
     * 
     * @param selectMap
     * @return
     */
    String getBmCkNewNoMax();

    /**
     * 根据参数得到正式表数据
     * 
     * @param param
     * @return
     */
    List<BmListGridDataDTO> getBmDataByJsonParam(BmJsonParamDTO param);

    /**
     * 根据参数得到正式表 数据量
     * 
     * @param param
     * @return
     */
    Long getBmCountByJsonParam(BmJsonParamDTO param);

    /**
     * 得到01 02 03类型的bm数量（正式）
     * 
     * @param bmType
     * @param bmCode
     * @return
     */
    Integer getBmNumber(@Param("bmType") String bmType, @Param("bmCode") String bmCode);

    /**
     * 得到04类型的bm数量（正式）
     * 
     * @param bmType
     * @param bmCode
     * @return
     */
    Integer getBmNumberTypeFour(@Param("bmType") String bmType, @Param("bmCode") String bmCode);

    /**
     * 得到单品的m和c的基本信息，为什么把这个方法放到这里，是因为 分支开发实在是不灵活导致的！！被逼的！！
     * 
     * @param itemCode
     * @param stroe
     * @return
     */
    List<BmItemMandCInofDto> getBmItemMandCInof(@Param("itemCode") String itemCode,
            @Param("store") String store);

    /**
     * 根据newNo得到his表中的最大的newNosub值
     * 
     * @param newNo
     * @return
     */
    String getNewNoSubByNo(@Param("newNo") String newNo);

    /**
     * 根据Item Barcode得到商品基本信息
     * 
     * @param item1
     * @return
     */
    IyItemM getItemByCode(String item1);

    /**
     * 根据检索参数得到 bm正式表中excel导出的数据
     * 
     * @param bmJsonParam
     * @return
     */
    List<BmOutExcel> getBmOutExcelData(BmJsonParamDTO bmJsonParam);
}