package cn.com.bbut.iy.itemmaster.config;

import cn.com.bbut.iy.itemmaster.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shiy
 */
@Component
@SpringBootTest(classes = Application.class)
public class ExcelConfigTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ThreadPoolTaskExecutor transExecutor;

    @Test
    public void testTransExecutor() {
        Assert.assertNotNull(transExecutor);
    }
}