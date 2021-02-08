package cn.com.bbut.iy.itemmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author shiy
 */
@SpringBootTest(classes = Application.class)
public class TestCache extends AbstractTestNGSpringContextTests {

    public static final String CACHENAME = "test1";

    @Autowired
    private TestService service;

    @Autowired
    private CacheManager manager;

    @Test
    public void testCacheNames() {

        String id = service.foo("id1");
        System.out.println(manager.getCache(CACHENAME).getName());
        service.foo("id2");
        service.foo("id1");
        service.foo("id2");

    }

}


