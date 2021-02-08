package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RVItemsGridResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String barcode;//商品条码
    private String articleId;//商品编号
    private String articleName;//商品名称
    private String unitId;//单位编号
    private String unitName;//单位
    private String spec;//规格
    private String orderQty;//退货数量
    private String orderActualQty;//退货实际数量
    private String orderPrice;//价格
    private String shipmentCd;
    private String orderAmount;//退货总金额
    private String reason;//退货理由
    //追加
}
