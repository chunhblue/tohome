package cn.com.bbut.iy.itemmaster.dto.bm;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order 明细数据展示 <br>
 * 此类对象中各个字段均为编辑画面中 隐藏域中input的结构， <br>
 * 所有字段均为字符串，各字段对应input所有所需的属性
 * 
 * @author songxz
 * @date:
 */
@Data
@NoArgsConstructor
public class BmViewDetailDTO {

    private String id;
    private String store;
    private String bmcode;
    private String itemsystem;
    private String itemcode;
    private String itemname;
    private String dpt;
    private BigDecimal costtax;
    private BigDecimal pricetax;
    private String status;
    private BigDecimal bmprice;
    private BigDecimal disprice;
    private BigDecimal profitrate;
    private BigDecimal buycount;
    private BigDecimal discount;
    private String ab;
    private String value;

    public BmViewDetailDTO(String id, String store, String bmcode, String itemsystem,
            String itemcode, String itemname, String dpt, BigDecimal costtax, BigDecimal pricetax,
            String status, BigDecimal bmprice, BigDecimal disprice, BigDecimal profitrate,
            BigDecimal buycount, BigDecimal discount, String ab, String value) {
        super();
        this.id = id;
        this.store = store;
        this.bmcode = bmcode;
        this.itemsystem = itemsystem;
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.dpt = dpt;
        this.costtax = costtax;
        this.pricetax = pricetax;
        this.status = status;
        this.bmprice = bmprice;
        this.disprice = disprice;
        this.profitrate = profitrate;
        this.buycount = buycount;
        this.discount = discount;
        this.ab = ab;
        this.value = value;
    }

}
