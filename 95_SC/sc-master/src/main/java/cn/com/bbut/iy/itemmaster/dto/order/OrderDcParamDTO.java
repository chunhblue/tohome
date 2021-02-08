package cn.com.bbut.iy.itemmaster.dto.order;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * order 商品
 * 
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDcParamDTO extends GridParamDTO {
    /**
     * 商品编号
     */
    private String articleId;
    /**
     * 订货开始时间
     */
    private String orderStartDate;
    /**
     * 订货结束时间
     */
    private String orderEndDate;
    /**
     * 订货时间
     */
    private String orderDate;
    /**
     * 业务时间
     */
    private String businessDate;
    /**
     * 传票号
     */
    private String orderId;
    /**
     * 传票状态
     */
    private String orderSts;
    /**
     * 传票类型
     */
    private String orderType;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * dcId
     */
    private String dcId;
    /**
     * 订货页面状态
     */
    private int use;
    /**
     * 店铺cds
     */
    private Collection<String> storeCds;

    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    /*审核状态*/
    private String reviewStatus;

    /**
     * 模糊框搜索值
     */
    private String v;

    // 分页
    private int limitStart;
}
