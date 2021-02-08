package cn.com.bbut.iy.itemmaster.dto.bm;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;

/**
 * order 采购编辑-提交对象
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmSubmitDataDTO implements Serializable {

    private static final long serialVersionUID = 2019L;
    // 登录序列号(主档和明细共有的属性)
    private String newNo;
    // 基本信息 根据优惠店铺切分了集合
    private List<IyBmCk> baseBmList;
    // 单品集合信息
    private List<IyBmItemCk> bmItems;
    // 操作人身份 1：采购样式，2:事业部部长，3：系统部，4：店铺
    private Integer identity;
    // 当前画面类型 0：Add，1：修改，2：审核，3：查看
    private Integer pageType;
}
