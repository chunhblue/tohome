package cn.com.bbut.iy.itemmaster.dto.bm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * bm登录人的基本信息
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmUserInfoDTO {
    // 采购、店铺 人员id
    private String userId;
    // 事业部
    private String div;
    private String divDpt;
    private String divName;
    // 部
    private String dep;
    private String depDpt;
    private String depName;
    // dpt
    private String dpt;
    private String dptDpt;
    private String dptName;
    // 店铺
    private String store;
    private String storeName;

}
