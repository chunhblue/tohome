package cn.com.bbut.iy.itemmaster.entity;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeInventoryQuery extends GridParamDTO {

    private static final long serialVersionUID = 1L;

    // 分页
    private int limitStart;
    // 数据库的字段
    private String ItemCode; // 商品编码
    private  String ItemBarcode;     // 商品条码
    private String ItemName;       // 商品名称
    private String Specification; // 规格
    private String UOM;   // 单位
    private String onHandQty;    // 昨日库存数量
    private String saleQty;      // 当日销售数量
    private String receiveQty; // 当日库存调整数量
    private String adjustQty; // 在途数量
    private String QuantityInTransit; // 实时库存数量
    private String onOrderQty; // 在途数量
    private String WriteOffQuanity; // 冲销数量
    private String TransferQuantity; // 转账数量

}
