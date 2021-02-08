package cn.com.bbut.iy.itemmaster.dto.bm;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order 明细数据展示 基础内容部分
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmViewBaseDTO {
    // bm类型
    private String bmType;
    // bm编号
    private String bmCode;
    // 销售起始日期
    private String startDate;
    // 销售结束日期
    private String endDate;
    // 店铺带名字的集合
    private String storeList;
    // 店铺只有店铺号的
    private String stores;
    // bm数量
    private BigDecimal num;
    // order 折扣
    private BigDecimal discountRate;
    // order 商品价格
    private BigDecimal bmPrice;

}
