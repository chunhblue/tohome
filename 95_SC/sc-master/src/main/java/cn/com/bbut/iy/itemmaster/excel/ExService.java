/**
 * ClassName  ExService
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-7-5
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.File;
import java.io.IOException;


/**
 * 统一的excel转换service 注意：此处如果放到service包中，有可能因为db连接不够造成死锁
 * 
 * @author Shiy
 */

public interface ExService {
    /** 统一调用接口 */
    File getExcel(BaseExcelParam param) throws IOException;
}
