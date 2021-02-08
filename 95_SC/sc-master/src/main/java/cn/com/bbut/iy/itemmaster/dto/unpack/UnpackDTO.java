package cn.com.bbut.iy.itemmaster.dto.unpack;
 
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
 
/**
 * 拆包销售头档DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UnpackDTO extends GridDataDTO {
 
    // 店铺编号
    private String storeCd;
    private String storeName;
 
    // 拆包编号
    private String unpackId;
 
    // 母货号
    private String parentArticleId;
    private String articleName;
 
    // 拆包日期
    private String unpackDate;
 
    // 母商品条码
    private String barcode;
 
    // 单位数量
    private BigDecimal unitQty;
 
    // 数量
    private BigDecimal unpackQty;
 
    // 成本
    private BigDecimal unpackCost;
 
    // 售价
    private BigDecimal salesPrice;
 
    // 总金额
    private BigDecimal unpackAmt;
 
    // 拆包类型
    private String unpackType;
 
    // 摘要
    private String remarks;
 
    // 状态
    private String repackageStatus;
 
    // 备份状态
    private String backupStatus;
 
    // 日结标识
    private String nrFlg;
 
    // 共通DTO
    private CommonDTO commonDTO;
}