package cn.com.bbut.iy.itemmaster.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传票查询店铺信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfoDTO{

    // 店铺编号
    private String storeCd;

    // 店铺名称
    private String storeName;
}
