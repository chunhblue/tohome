package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor;

import lombok.Data;

/**
 * @author lz
 *
 */
@Data
public class RVItemsParam {
    private String orderId;
    private String articleId;

    private int limitStart;
    private int limitEnd;
    private String orderByClause;
}
