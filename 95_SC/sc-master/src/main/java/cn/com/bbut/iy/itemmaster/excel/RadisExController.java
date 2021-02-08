/**
 * ClassName  DefaultExController
 * <p>
 * History
 * Create User: Shiy
 * Create Date: 2013-7-5
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Shiy
 */
@Service
@Slf4j
public class RadisExController implements ExController {

    /** redis中用于保存excel导出信息的缓存名 */
    private static final String EXCACHENAME = "exCache";

    /** 运行超时时间（分钟） */
    public static int TIMEOUTMINUTES = 15;

    /** 等待时查询的间隔时间 */
    public static long WAITINTERVAL = 5000L;

    @Autowired
    private RedisCacheManager manager;

    @Autowired
    @Qualifier("transExecutor")
    private ThreadPoolTaskExecutor executor;

    @Autowired
    @Qualifier("waitExecutor")
    private ThreadPoolTaskExecutor waitExecutor;

    private static final Lock lock = new ReentrantLock();

    private static final Lock writeLock = new ReentrantLock();

    /**
     * @see cn.com.bbut.iy.itemmaster.excel.ExController#getStatus(String)
     */
    @Override
    public ExStatus getStatus(String key) {
        Cache cache = manager.getCache(EXCACHENAME);
        if (cache.get(key) == null) {
            return null;
        } else {
            return (ExStatus) cache.get(key).get();
        }
    }

    /**
     * @see cn.com.bbut.iy.itemmaster.excel.ExController#updateStatus(String,
     *      cn.com.bbut.iy.itemmaster.excel.ExStatus)
     */
    @Override
    public void updateStatus(String key, ExStatus status) {
        writeLock.lock();
        try {
            Cache cache = manager.getCache(EXCACHENAME);
            cache.put(key, status);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * @see cn.com.bbut.iy.itemmaster.excel.RadisExController#executeTask(Callable)
     */
    @Override
    public void executeTask(Callable<ExStatus> task) {
        FutureTask<ExStatus> future = new FutureTask<>(task);
        executor.execute(future);
    }

    /**
     * 2019.08.14 此方法原用于延迟等待处理结果的返回，现在由画面或调用方 自行轮询查询，
     *
     * @see cn.com.bbut.iy.itemmaster.excel.RadisExController#executeWait(String)
     */
    @Override
    @Deprecated
    public ExStatus executeWait(final String key) {
        FutureTask<ExStatus> future = new FutureTask<>(new Callable<ExStatus>() {
            @Override
            public ExStatus call() throws Exception {
                while (getStatus(key) == null
                        || getStatus(key).getStatus() != ExStatus.STATUS_FINISHED) {
                    Thread.sleep(WAITINTERVAL);
                }
                return getStatus(key);
            }
        });
        waitExecutor.execute(future);
        ExStatus status = null;
        try {
            status = future.get(TIMEOUTMINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException e) {
            log.error(key + " wait err", e);
        } catch (TimeoutException e) {
            log.error(key + " wait timeout", e);
        }

        return status;
    }

    /**
     * @see cn.com.bbut.iy.itemmaster.excel.RadisExController#checkAndRun(String,
     *      ExService, cn.com.bbut.iy.itemmaster.excel.BaseExcelParam)
     */
    @Override
    public ExStatus checkAndRun(final String key, ExService service, final BaseExcelParam param) {
        boolean isCreated = false;
        ExStatus status = null;
        boolean isRunning = false;
        log.debug("ready to checking...{}", key);
        lock.lock();
        try {
            log.debug("checking...{}", key);
            status = getStatus(key);
            if (status != null) {
                switch (status.getStatus()) {
                case ExStatus.STATUS_RUNNING:
                    isRunning = true;
                    isCreated = false;
                    break;
                case ExStatus.STATUS_FINISHED:
                    // 此处可增加判断文件是否存在
                    isCreated = true;
                    break;
                case ExStatus.STATUS_FAILED:
                    updateStatus(key,
                            new ExStatus(ExStatus.STATUS_RUNNING, null, param.getExFileName()));
                    isCreated = false;
                    break;
                default:
                    throw new RuntimeException("unknown status " + status.getStatus());
                }
            } else {
                updateStatus(key,
                        new ExStatus(ExStatus.STATUS_RUNNING, null, param.getExFileName()));
                isCreated = false;
            }
        } finally {
            lock.unlock();
        }
        log.debug("isRunning:{}", isRunning);
        if (isRunning) {
            log.debug("{} is already running", key);
            // log.debug("{} is running,start to wait", key);
            // status = executeWait(key);
            // if (status != null && status.getStatus() ==
            // ExStatus.STATUS_FINISHED) {
            // log.debug("{} wait and success", key);
            // isCreated = true;
            // } else {
            // log.debug("{} wait but failed", key);
            // }
            return status;
        }
        if (!isCreated) {
            log.debug("{} start to create", key);
            ExTask task = new ExTask(service, param);
            final FutureTask<ExStatus> future = new FutureTask<>(task);
            log.debug("executor active count:{}", executor.getActiveCount());
            try {
                executor.execute(future);
            } catch (TaskRejectedException tske) {
                log.error(key + "任务列队满，拒绝执行", tske);
                ExStatus sts = new ExStatus(ExStatus.STATUS_REJECTED, null, param.getExFileName());
                updateStatus(key, sts);
                return sts;
            }
            waitExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ExStatus sts;
                    try {
                        sts = future.get(TIMEOUTMINUTES, TimeUnit.MINUTES);
                        updateStatus(key, sts);
                        log.debug("{} created and status is {}(1:running 2:finished 3:failed)", key,
                                sts.getStatus());
                    } catch (Exception e) {
                        log.error(key + "err", e);
                        sts = new ExStatus(ExStatus.STATUS_FAILED, null, param.getExFileName());
                        updateStatus(key, sts);
                        log.debug("{} create failed", key);
                    }
                }
            });

        }
        return status;
    }

}
