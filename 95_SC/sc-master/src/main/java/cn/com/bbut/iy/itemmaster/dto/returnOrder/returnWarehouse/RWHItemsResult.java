package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import lombok.Data;

/**
 * Created by lz on 2020/1/14.
 */
@Data
public class RWHItemsResult {
    // 数据库查询出的字段
    private String barcode;//商品条码
    private String articleId;//商品编号
    private String articleName;//商品名称
    private String unitId;//单位编号
    private String unitName;//单位
    private String spec;//规格
    private String orderQty;//退货数量
    private String orderPrice;//价格
    private String shipmentCd;
    private String orderAmount;//退货总金额
    private String reason;//退货理由
}
