直接在代码上获取

```java
package cn.com.bbut.iy.itemmaster.util;

import org.junit.Test;
import org.junit.jupiter.api.DynamicTest;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {

    @Test
    public static void Test1(){
        String version = SpringVersion.getVersion();
        String version1 = SpringBootVersion.getVersion();
        System.out.println(version);
        System.out.println(version1);
    }

    public static void main(String[] args) {
        Test1();
    }
}

out:
5.1.3.RELEASE
2.1.1.RELEASE
```

