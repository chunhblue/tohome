package cn.com.bbut.iy.itemmaster.dto.unpack;
 
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
 
/**
 * 拆包销售明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UnpackDetailsDTO extends GridDataDTO {
 
    // 拆包编号
    private String unpackId;
 
    // 店铺编号
    private String storeCd;
 
    // 母货号
    private String parentArticleId;
 
    // 子货号
    private String childArticleId;
    private String articleName;
 
    // 序号
    private String serialNo;
 
    // 子货号条码
    private String barcode;
 
    // 单位数量
    private BigDecimal unitQty;
 
    // 数量
    private BigDecimal unpackQty;
 
    // 成本
    private BigDecimal unpackCost;
 
    // 售价
    private BigDecimal salesPrice;
 
    // 共通DTO
    private CommonDTO commonDTO;
 
}