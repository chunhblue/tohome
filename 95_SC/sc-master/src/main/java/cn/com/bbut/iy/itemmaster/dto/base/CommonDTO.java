package cn.com.bbut.iy.itemmaster.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作表用户、时间共通字段
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonDTO{

    /**
     * 创建员工ID
     */
    private String createUserId;

    /**
     * 创建年月日
     */
    private String createYmd;

    /**
     * 创建时分秒
     */
    private String createHms;

    /**
     * 修改员工ID
     */
    private String updateUserId;

    /**
     * 修改年月日
     */
    private String updateYmd;

    /**
     * 修改时分秒
     */
    private String updateHms;

    /**
     * 更新画面ID
     */
    private String updateScreenId;

    /**
     * 更新IP地址
     */
    private String updateIpAddress;

}
