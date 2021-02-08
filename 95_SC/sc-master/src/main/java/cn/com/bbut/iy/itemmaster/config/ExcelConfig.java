package cn.com.bbut.iy.itemmaster.config;

import cn.com.bbut.iy.itemmaster.excel.ExRejectedExecutionHandler;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author guok
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "iy.excel")
public class ExcelConfig {

    private String fileDir;

    private ThreadPoolProperties transPool = new ThreadPoolProperties();

    private ThreadPoolProperties waitPool = new ThreadPoolProperties();

    @Bean(name = "transExecutor")
    public ThreadPoolTaskExecutor transExecutor() {
        return getThreadPoolTaskExecutor(transPool);
    }

    @Bean(name = "waitExecutor")
    public ThreadPoolTaskExecutor waitExecutor() {
        return getThreadPoolTaskExecutor(waitPool);
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor(ThreadPoolProperties waitPool) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(waitPool.getCorePoolSize());
        taskExecutor.setMaxPoolSize(waitPool.getMaxPoolSize());
        taskExecutor.setKeepAliveSeconds(waitPool.getKeepAliveSeconds());
        taskExecutor.setQueueCapacity(waitPool.getQueueCapacity());
        taskExecutor.setRejectedExecutionHandler(new ExRejectedExecutionHandler());
        return taskExecutor;
    }

    @Data
    private class ThreadPoolProperties {

        private Integer corePoolSize;

        private Integer maxPoolSize;

        private Integer keepAliveSeconds;

        private Integer queueCapacity;
    }
}
