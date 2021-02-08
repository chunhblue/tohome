/**
 * ClassName  ExRejectedExecutionHandler
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-7-6
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Shiy
 */
@Slf4j
public class ExRejectedExecutionHandler implements RejectedExecutionHandler {

    /**
     * @see RejectedExecutionHandler#rejectedExecution(Runnable,
     *      ThreadPoolExecutor)
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.error("ThreadPool is full:{}", executor.getMaximumPoolSize());
        throw new RejectedExecutionException();
    }

}
