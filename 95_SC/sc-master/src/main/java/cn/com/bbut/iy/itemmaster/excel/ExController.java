/**
 * ClassName  ExController
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-7-5
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.util.concurrent.Callable;

/**
 * excel转换控制模块，主要用于多线程控制 注意：此处如果放到service包中，有可能因为db连接不够造成死锁
 * 
 * @author Shiy
 */

public interface ExController {

    /**
     * 取指定key的转换状态
     * 
     * @param key
     * @return
     */
    ExStatus getStatus(String key);

    /**
     * 更新指定key的转换状态
     * 
     * @param key
     * @param status
     */
    void updateStatus(String key, ExStatus status);

    /**
     * 执行指定task（如：转换、等待）
     * 
     * @param task
     */
    void executeTask(Callable<ExStatus> task);

    /**
     * 线程执行等待
     */
    ExStatus executeWait(final String key);

    /**  */
    ExStatus checkAndRun(String key, ExService service, BaseExcelParam param);
}
