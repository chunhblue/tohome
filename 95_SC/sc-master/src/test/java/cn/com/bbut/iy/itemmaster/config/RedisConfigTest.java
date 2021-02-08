package cn.com.bbut.iy.itemmaster.config;

import static org.testng.Assert.assertEquals;

import cn.com.bbut.iy.itemmaster.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;

/**
 * @author shiy
 */
@Component
@SpringBootTest(classes = Application.class)
@Slf4j
public class RedisConfigTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestCacheService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("sessionRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConnectionFactory factory;

    @Test
    public void testCache() {
        String name = service.getNameById("1");
        assertEquals("1name", name);
        log.debug("-------------------------");
        name = service.getNameById("1");
        assertEquals("1name", name);
        log.debug("-------------------------");
        name = service.getNameById("2");
        assertEquals("2name", name);
        log.debug("-------------------------");
        name = service.getNameById("2");
        assertEquals("2name", name);
    }

    @Test
    public void testCacheObject() {
        User user = new User();
        user.setUserId("testid");
        User cachedUser = service.getUser(user);
        assertEquals(cachedUser, user);
        cachedUser = service.getUser(user);
        assertEquals(cachedUser, user);
    }

    @Test
    public void testSessionRedis() {
        redisTemplate.opsForValue().append("testKey", "testValue");
        Assert.assertEquals(redisTemplate.opsForValue().get("testKey"), "testValue");
    }

}