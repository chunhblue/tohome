package cn.com.bbut.iy.itemmaster.dto.bm;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;

/**
 * @author songxz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BmListGridDataDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;

    // 采购-事业部长（商品部长）-系统部-店铺-bm编码
    private String bmCode;
    // 采购-事业部长（商品部长）-系统部-店铺-bm数量
    private Integer bmCount;
    // 采购-事业部长（商品部长）-系统部-店铺-区分ab组
    private String bmType;
    private String bmTypeText;
    // 采购-事业部长（商品部长）-系统部-店铺-销售开始日
    private String startDate;
    // 采购-事业部长（商品部长）-系统部-店铺-销售结束日
    private String endDate;
    // 采购-事业部长（商品部长）-系统部-店铺-发起部门
    private String createDpt;
    // 采购-事业部长（商品部长）-系统部-采购员编码
    private String buyer;
    // 采购-事业部长（商品部长）-系统部-采购员名称
    private String buyerName;
    // 事业部长（商品部长）-毛利率
    private BigDecimal bmGross;
    private Integer bmNumber;
    // 店铺-店铺号
    private String stroe;
    // 店铺-Item Barcode
    private String itemCode;
    private Integer numA;
    private Integer numB;
    // 采购-驳回理由
    private String rejectreason;
    // key标记，表示该行的 bm类型和code，用01#123来展示
    private String trKey;

    // 0-正常 1-紧急
    private String forstFlg;
    // 资源等内容
    private String dptAll;

    // 查看权限标志位 5位，每位代表一个事业部，未审核时为0，审核时为1
    private String rightFlg;

    // 事业部长（商品部长）- 系统部-新登BM优先度
    private String firstFlg;

    // 事业部长（商品部长）-系统部- (Add标志)操作类型
    private String newFlg;
    private String newFlgText;

    // 采购-事业部长（商品部长）-系统部-状态(操作类型)
    private String opFlg;
    private String opFlgText;

    // 审核状态
    private String checkFlg;
    private String checkFlgText;

    // 0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
    private String updateFlg;

    // 可以评审标记，0代表可以，1不可以。
    private String canReview;
    // 评审标记为0时，此字段保存可以评审的资源标记，事业部级别，例如199,299等，使用逗号分隔
    private String checkResources;

}
