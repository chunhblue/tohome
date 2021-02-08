package cn.com.bbut.iy.itemmaster.dto.bm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * BM excel 导出数据用
 * 
 * @author songxz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BmOutExcel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String no;
    // 发起部门PDT
    private String createDpt;
    // bm类型 B/M商品分类
    private String bmType;
    // bm编码
    private String bmCode;
    // 销售开始日
    private String startDate;
    // 销售结束日
    private String endDate;
    // bm折扣率
    private BigDecimal bmDiscountRate;
    // bm数量
    private Integer bmCount;
    // bm销售价格
    private BigDecimal bmPrice;
    // 店铺号
    private String store;
    // 单品dpt
    private String itemDpt;
    // 单品条码
    private String itemCode;
    // 单品 系统
    private String itemSystem;
    // 单品名称
    private String itemName;
    // 单品售价
    private BigDecimal itemPrice;
    // 单品折扣售价
    private BigDecimal itemPriceDisc;
    // 单品毛利率
    private BigDecimal itemGross;
    // A组数量
    private Integer numA;
    // B组数量
    private Integer numB;
    // 审核状态
    private String checkFlg;
    // 审核区分
    private String newFlg;
    // 修改标志
    private String updateFlg;
    // 操作类型
    private String opFlg;
    // 采购员编码
    private String buyer;
    // 采购员名称
    private String buyerName;
    // 单品确认状态
    private String bmItemFlg;
    // 驳回原因
    private String rejectreason;

    // 操作人
    private String userId;
    // 操作人名称
    private String userName;
    // 操作时间
    private Date updateDate;

}
