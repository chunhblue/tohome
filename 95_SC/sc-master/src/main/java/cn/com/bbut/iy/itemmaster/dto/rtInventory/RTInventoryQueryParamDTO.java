package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RTInventoryQueryParamDTO extends GridParamDTO {

    /**
     * 检索参数json格式，前端传过来后，使用 RTInventoryQueryParamDTO 进行反射解析
     */
    private String searchJson;

    // 业务日期
    private String businessDate;

    // 分页
    private int limitStart;

    private String itemCode; // 商品编码
    private String itemBarcode;     // 商品条码
    private String itemName;       // 商品名称
    private String specification; // 规格
    private String uom;   // 单位
    private BigDecimal onHandQty;    // 昨日库存数量
    private BigDecimal saleQty;      // 当日销售数量
    private BigDecimal receiveQty; // 当日收货数量
    private BigDecimal adjustQty; // 当日库存调整数量
    private BigDecimal realtimeQty; // 实时库存数量
    private BigDecimal onOrderQty; // 在途数量

    private String stockDate;   // 库存日期
    private String depId;   // 部门编号
    private String pmaId;   // 大分类编号
    private String categoryId;   // 中分类编号
    private String subCategoryId;   // 小分类编号
    private String vendorId;   // 供应商编号

    // 是否分页
    private boolean flg = true;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private String  omCode;
    private String ofcCode;
    private String ocCode;

    // Es查询出来的所有商品id
    private Collection<String> articles;

}
