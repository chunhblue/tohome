package cn.com.bbut.iy.itemmaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.servlet.MultipartConfigElement;

/**
 * @author shiy
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(value = { "cn.shiy.common.pmgr.dao", "cn.com.bbut.iy.itemmaster.dao" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
