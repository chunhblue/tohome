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

import java.io.IOException;

/**
 * 继承excel转换service 注意：此处如果放到service包中，有可能因为db连接不够造成死锁
 * 
 * @author songxz
 */

public interface ExtendExService extends ExService {

    /** 执行ExService.getExcel 的后置方法 */
    void postExcel(BaseExcelParam param) throws IOException;

    /** 执行ExService.getExcel 的 前置方法 */
    void preExcelExcel(BaseExcelParam param) throws IOException;
}
