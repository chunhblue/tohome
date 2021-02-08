package cn.com.bbut.iy.itemmaster;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author shiy
 */
@Service
public class TestService {

    @Cacheable(TestCache.CACHENAME)
    public String foo(String id) {
        System.out.println("foo with " + id);
        return id;
    }
}
