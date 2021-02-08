package cn.com.bbut.iy.itemmaster.dto.sa0050;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * SA0050grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SA0050GridDto  extends GridDataDTO {

    /**
     * 员工号
     */
    private String cashierId;

    /**
     *  姓名
     */
    private String cashierName;

    /**
     *  职务
     */
    private String duty;

    /**
     *  权限级别cd
     */
    private String cashierLevelCd;

    /**
     *  权限级别
     */
    private String cashierLevel;

    /**
     *  权限参数
     */
    private String cashierParameter;

    /**
     *密码
     */
    private String cashierPassword;

    /**
     * 生效状态
     */
    private String effectiveSts;

    /**
     * 生效状态
     */
    private String effectiveStsName;


    /**
     *门店cd
     */
    private String storeCd;

    /**
     *门店name
     */
    private String storeName;
    private String cashierEmail;

    // 门店ID + Name
    private String store;
    // Area Mranager
    private String ofc;

    private String ofcName;
}
