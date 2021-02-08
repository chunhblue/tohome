/**
 * ClassName  ExStatus
 *
 * History
 * Create User: Shiy
 * Create Date: 2019-11-11
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * excel转换的状态枚举
 * 
 * @author Shiy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 执行中 */
    public static final short STATUS_RUNNING = 1;

    /** 已完成 */
    public static final short STATUS_FINISHED = 2;

    /** 失败 */
    public static final short STATUS_FAILED = 3;

    /** 队列满，拒绝 */
    public static final short STATUS_REJECTED = 4;

    /** status */
    private short status = STATUS_RUNNING;

    /** 转换完成后的文件名(指定目录下) */
    private String filename;
    /** 下载时的文件名 */
    private String exFileName;

}
