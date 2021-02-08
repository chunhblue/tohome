/**
 * ClassName  BaseExcelParam
 *
 * History
 * Create User: Shiy
 * Create Date: 2019-11-11
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * excel传参用DTO
 * 
 * @author Shiy
 */
@Data
public class BaseExcelParam implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 下载时的文件名 */
    private String exFileName;

    /** 标题行 */
    List<String> headers;
}
