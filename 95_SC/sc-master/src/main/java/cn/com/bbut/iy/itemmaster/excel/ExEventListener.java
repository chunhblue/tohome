/**
 * ClassName  InitListener
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-2-4
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel文档转换事件监听
 * 
 * @author Shiy
 */

public interface ExEventListener {

    /**
     * 执行接口
     * 
     * @param wb
     *            当前workbook
     */
    void action(Workbook wb);
}
