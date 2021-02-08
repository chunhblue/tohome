package cn.com.bbut.iy.itemmaster.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import org.testng.annotations.Test;

/**
 * @author shiy
 */
public class IyCacheKeyGeneratorTest {

    @Test
    public void testGenerate() {
        // IyCacheKeyGenerator generator = new IyCacheKeyGenerator();
        //
        // Assert.assertEquals(0, generator.generate(null, null, null));
        //
        // HashObj obj = HashObj.builder().age(1).name("张三").date("d1", new
        // Date())
        // .date("d2", new Date()).build();
        //
        // System.out.println(generator.generate(null, null, obj));
        //
        // System.out.println(generator.generate(null, null, obj, obj));
    }

    @Test
    public void testHash() throws UnsupportedEncodingException {
        // String str = "测试字符abc123";
        // // System.out
        // // .println((Hashing.murmur3_32().hashString(str,
        // // Charset.forName("UTF-8")).asInt()));
        // System.out.println(MurmurHash3.hash32(str));
        //
        // HashObj obj = HashObj.builder().age(1).name("张三").date("d1", new
        // Date())
        // .date("d2", new Date()).build();
        // System.out.println("*****************");
        // System.out.println(obj.toString());
        // // System.out.println((Hashing.murmur3_32()
        // // .hashString(obj.toString(), Charset.forName("UTF-8")).asInt()));
        // System.out.println(MurmurHash3.hash32(obj.toString()));
        // System.out.println(MurmurHash3.hash64(obj.toString().getBytes("UTF-8")));

    }

    @Builder
    @Data
    public static class HashObj {
        private String name;
        private int age;
        @Singular
        private Map<String, Date> dates;
    }
}