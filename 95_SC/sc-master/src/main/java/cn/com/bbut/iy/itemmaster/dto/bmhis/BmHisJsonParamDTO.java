package cn.com.bbut.iy.itemmaster.dto.bmhis;

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
public class BmHisJsonParamDTO {

    private String newNo;
    private String newNoSub;

    // 事业部
    private String div;
    // dpt 实际是部门内容
    private String dpt;
    // 店铺
    private String store;
    // 审核区分
    private String newFlg;
    // 操作类型
    private String opFlg;
    // bm类型 01 捆绑，02 混合，03 固定组合，04 阶梯折扣，05 AB组
    private String bmType;
    // order 编号
    private String bmCode;
    // 采购员
    private String buyer;
    // 单品条码 item1
    private String item;
    // 销售开始日期
    private String startDate;
    // 销售结束日期
    private String endDate;

    private int limitStart;
    private int limitEnd;

    private String orderByClause;

    private List<IyResourceDTO> resources;

}
