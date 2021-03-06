package cn.com.bbut.iy.itemmaster.dto.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 仓库配送验收头档查询参数DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDetailsParamDTO extends GridParamDTO {
    private static final long serialVersionUID = 1L;

    private String storeCd;// 门店编号

    private String orderId;// 单据编号

    private String serialNo;// 序号

    private String itemId;// 商品编号

    private String barcode;// 商品条码

    private String itemName;// 商品名称

    private String businessDate;// 业务日期

    /*
     * 判读订货单状态是查看or收货编辑
     * 查看('0')：显示已收货数量
     * 编辑('1')：默认显示订货数量
     */
    private String pageSts;

    private int limitStart;// 分页

    private String physicalReceivingDate;
}
