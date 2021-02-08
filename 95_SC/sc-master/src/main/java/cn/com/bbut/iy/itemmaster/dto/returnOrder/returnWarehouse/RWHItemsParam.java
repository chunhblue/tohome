package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import lombok.Data;

/**
 * @author lz
 *
 */
@Data
public class RWHItemsParam {
    private String orderId;
    private String articleId;

    private int limitStart;
    private int limitEnd;
    private String orderByClause;
}
