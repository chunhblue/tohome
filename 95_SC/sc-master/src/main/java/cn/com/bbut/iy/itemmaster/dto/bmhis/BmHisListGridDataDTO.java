package cn.com.bbut.iy.itemmaster.dto.bmhis;

import java.math.BigDecimal;
import java.util.Date;

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
public class BmHisListGridDataDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;

    private Integer od;
    private String newNo;
    private String newNoSub;
    // 跳转明细专用key，由 bmType+newNo+newNoSub拼接
    private String key;
    // 采购-事业部长（商品部长）-系统部-店铺-bm编码
    private String no;
    // 状态
    private String status;
    // 状态text
    private String statusText;
    // 区分
    private String bmType;
    // 区分text
    private String bmTypeText;

    // 发起部门DPT
    private String createDpt;
    // 店铺号 经过确认 列表中不显示店铺号
    private String store;
    // bm编码
    private String bmCode;
    // bm数量
    private Integer bmCount;
    // bm销售价格
    private BigDecimal bmPrice;
    // 销售开始日
    private String startDate;
    // 销售结束日
    private String endDate;
    // 审核区分
    private String newFlg;
    // 审核区分Text
    private String newFlgText;
    // 操作类型
    private String opFlg;
    // 操作类型text
    private String opFlgText;
    // 操作人（名称）
    private String userName;
    // 更新时间
    private Date updateDate;
    // 驳回理由
    private String rejectreason;

    private Integer numA;
    private Integer numB;

}
