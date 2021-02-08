package cn.com.bbut.iy.itemmaster.service.base;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ItemStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyItemM;

/**
 * 商品（单品）主挡
 * 
 * @author songxz
 */
public interface IyItemMService {
    /**
     * 根据商品名称或商品编码模糊查询到商品集合
     * 
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getItemLikeNameCode(String v, String... notv);

    /**
     * 使用Item Barcode是否存在
     * 
     * @param item
     * @return
     */
    boolean isItemExist(String item);

    /**
     * 根据Item Barcode和店铺号取得c表数据
     * 
     * @param itemSystem
     *            商品 系统码
     * @param st
     *            店铺号
     * @return
     */
    ItemStoreDTO getItemCInfo(String itemSystem, String store);

    /**
     * 根据单品条码取得单品M表数据
     * 
     * @param item1
     * @return
     */
    IyItemM getItemByCode(String item1);

    /**
     * 根据单品系统码得到商品信息
     * 
     * @param itemSystem
     * @return
     */
    IyItemM getItemInfoBySystem(String itemSystem);
}
