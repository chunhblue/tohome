package cn.com.bbut.iy.itemmaster.config;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.com.bbut.iy.itemmaster.annotation.FreemarkerComponent;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 注解注册标签
 * 
 * @author songxz
 * @date: 2019年10月10日 - 下午4:03:31
 */
@Component
public class CustomFreeMarkerConfigurer implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    Configuration configuration;

    @PostConstruct
    public void setSharedVariable() throws IOException, TemplateException {
        // 根据注解获取bean ,key is bean name ,value is bean object
        Map<String, Object> map = this.applicationContext
                .getBeansWithAnnotation(FreemarkerComponent.class);
        for (String key : map.keySet()) {
            configuration.setSharedVariable(key, map.get(key));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
