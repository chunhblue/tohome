package cn.com.bbut.iy.itemmaster.dto.bm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;

/**
 * 默认角色授权 参数dto
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmJsonParamDTO {

    // // identity 身份 0：身份混乱不可操作；:1：采购样式，2:大事业部(商品部长)部长 3：系统部，4：店铺样式
    private Integer identity;

    // 店铺人员登录时，该值为列表中的店铺列的内容，因为js处理会有效率问题
    private String store;

    // 数据来源表 0 正是表，1ck表，2历史表
    private Integer tableType;
    // bm商品状态
    private String bmStatus;
    // Add标志
    private String newFlg;
    // 修改标志 0-修改BM价格/折扣 1-修改BM生效期间 2-修改价格和期间
    private String updateFlg;
    // 审核状态
    private String checkFlg;
    // 事业部
    private String div;
    // dpt 实际是部门内容
    private String dpt;
    // 所属 0-自、 1-跨
    private String bmBePart;
    // bm类型 01 捆绑，02 混合，03 固定组合，04 阶梯折扣，05 AB组
    private String bmType;
    // order 编号
    private String bmCode;
    // 单品条码 item1
    private String itemCode;
    // 销售开始日期
    private String sellStartDate;
    // 销售结束日期
    private String sellEndDate;

    private int limitStart;
    private int limitEnd;

    private String orderByClause;

    private List<IyResourceDTO> resources;

}
