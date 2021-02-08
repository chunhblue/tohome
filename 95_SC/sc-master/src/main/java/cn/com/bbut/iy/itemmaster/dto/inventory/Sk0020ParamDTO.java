package cn.com.bbut.iy.itemmaster.dto.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 传票明细
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Sk0020ParamDTO extends GridParamDTO {

    // 业务日期
    private String businessDate;

    // 分页
    private int limitStart;

    // 店号
    private String storeCd;

    // 传票编号
    private String voucherNo;

    // 传票类型
    private String voucherType;

    // 传票日期
    private String voucherDate;

    // 商品编号
    private String articleId;

    // 对方店号
    private String storeCd1;

    // 调整原因
    private String adjustReason;

    // 店间调入的数量
    private BigDecimal qty1;

    // 店间调出的数量
    private BigDecimal qty2;

    private BigDecimal actualQty;

    // 店间调拨数量不同原因
    private String differenceReason;
}
