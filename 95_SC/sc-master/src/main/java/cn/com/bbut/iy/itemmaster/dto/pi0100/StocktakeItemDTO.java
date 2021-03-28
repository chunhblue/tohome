package cn.com.bbut.iy.itemmaster.dto.pi0100;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 盘点查询商品信息
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeItemDTO {
    private String barcode;
    private String storeCd;
    private String pmaCd;
    private String rowIndex;
    private String articleId;
    private String articleName;
    private String piCd;
    private String piDate;
    private String piStatus;
    // 生效开始日期
    private String effectiveStartDate;

    // 生效结束日期
    private String effectiveEndDate;
    // 单位
    private String uomCd;
    private String uom;

    private String badQty;

    // 规格
    private String spec;

    // 实时库存
    private BigDecimal realTimeQty;
    // 盘点期间的异动量
    private BigDecimal cQty;
    // 盘点差异数量
    private BigDecimal varianceQty;
    // 导出时间
    private String exportTime;
    //初盘品项数
    private String firstQty;
    //复盘品项数
    private String secondQty;
    // 销售数据
    private BigDecimal saleQtyTotal;
    // 销售退货数量
    private BigDecimal customerReturnQtyTotal;
    // 报废数量
    private BigDecimal scrapQty;
    // 调出数量
    private BigDecimal allocationQtyReduce;
    // 调入数量
    private BigDecimal allocationQtyAdd;
    // 收货数量
    private BigDecimal receiveTotalQty;
    // 退货数量
    private BigDecimal storeReturnQty;
    // 商品从盘点枪盘点的时间
    private String stocktakeTime;
    // 第一次异动量, 导出时候记录
    private BigDecimal cQty1;
    // 第二次异动量, 生成差异报表时记录
    private BigDecimal cQty2;
    // 上一次的差异量, 多次更新实时库存用
    private BigDecimal lastVarianceQty;

    // 临时存储数量
    private String piQty;
    // 拆包装转换比例
    private String converter;

    // region:Bad Merchandise;  Backroom;  Display
    private String region;
}
