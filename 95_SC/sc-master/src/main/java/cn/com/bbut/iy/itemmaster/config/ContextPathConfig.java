package cn.com.bbut.iy.itemmaster.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.com.bbut.iy.itemmaster.excel.ExRejectedExecutionHandler;

/**
 * @author guok
 */
@Configuration
@Data
public class ContextPathConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

}
