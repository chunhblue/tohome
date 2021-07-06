package cn.com.bbut.iy.itemmaster.dto.difference;

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
public class DifferenceItemsResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String barcode;
    private String articleId;
    private String articleName;
    private String unitName;
    private String orderQty;
    private String dcPickedQty;
    private String orderTax;
    private String orderPrice;
    private String receiveQty;
    private String adjustQty;
    private String adjustAmt;
    private String reason;
    private String orderedQty;
    private String orderqtyDcPickedQty;
}
