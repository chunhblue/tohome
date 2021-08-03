package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnWarehouseParamDTO extends GridParamDTO {
    private String orderRemark;
    /**
     * 订货区分
     */
    private String orderDifferentiate;
    /**
     * 退货时间
     */
    private String orderDate;
    /**
     * 单据号
     */
    private String orderId;
    /**
     * 原单据号
     */
    private String orgOrderId;
    /**
     * 店铺号
     */
    private String storeCd;
    /**
     * 店铺号
     */
    private String storeName;
    /**
     * 商品id
     */
    private String articleId;
    /**
     * 仓库号
     */
    private String dcNo;
    /**
     * 商品模糊搜索值
     */
    private String v;
    /**
     * 店铺权限
     */
    private Collection<String> stores;
   // 退货类型
    private String returnType;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
