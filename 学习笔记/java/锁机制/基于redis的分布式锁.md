### 基于redis的setnx()、get()、getset()方法 分布式锁

一.redis命令讲解：
setnx()命令：
setnx的含义就是SET if Not Exists，其主要有两个参数 setnx(key, value)。

该方法是原子的，如果key不存在，则设置当前key成功，返回1；如果当前key已经存在，则设置当前key失败，返回0。

get()命令：
get(key) 获取key的值，如果存在，则返回；如果不存在，则返回nil；
getset()命令：
这个命令主要有两个参数 getset(key, newValue)。该方法是原子的，对key设置newValue这个值，并且返回key原来的旧值。
假设key原来是不存在的，那么多次执行这个命令，会出现下边的效果：
\1. getset(key, “value1”) 返回nil 此时key的值会被设置为value1
\2. getset(key, “value2”) 返回value1 此时key的值会被设置为value2
\3. 依次类推！
二.具体的使用步骤如下：
\1. setnx(lockkey, 当前时间+过期超时时间) ，如果返回1，则获取锁成功；如果返回0则没有获取到锁，转向2。
\2. get(lockkey)获取值oldExpireTime ，并将这个value值与当前的系统时间进行比较，如果小于当前系统时间，则认为这个锁已经超时，可以允许别的请求重新获取，转向3。
\3. 计算newExpireTime=当前时间+过期超时时间，然后getset(lockkey, newExpireTime) 会返回当前lockkey的值currentExpireTime。
\4. 判断currentExpireTime与oldExpireTime 是否相等，如果相等，说明当前getset设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了，那么当前请求可以直接返回失败，或者继续重试。
\5. 在获取到锁之后，当前线程可以开始自己的业务处理，当处理完毕后，比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理。

具体代码如下：

```
<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
<dependency>    
	<groupId>redis.clients</groupId>    
	<artifactId>jedis</artifactId>   
	<version>3.0.0</version>
</dependency>
```

```java
package cn.com.bbut.iy.itemmaster.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Slf4j
@Service("distributedLockHandler")
public class DistributedLockHandler {
    private static final Integer Lock_Timeout = 5000;

    private Jedis jedis;

    /**
     * 外部调用加锁的方法
     * @param lockKey 锁的名字
     * @param timeout 超时时间（放置时间长度，如：5L）
     * @return
     */
    public boolean tryLock(String lockKey, Long timeout) {
        try {
            Long currentTime = System.currentTimeMillis();//开始加锁的时间
            boolean result = false;

            while (true) {
                if ((System.currentTimeMillis() - currentTime) / 1000 > timeout) {//当前时间超过了设定的超时时间
                    log.error("Execute DistributedLockHandler.tryLock method, Time out.");
                    break;
                } else {
                    result = innerTryLock(lockKey);
                    if (result) {
                        break;
                    } else {
                        log.error("Try to get the Lock,and wait 5 seconds....");
                        Thread.sleep(5000);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to run DistributedLockHandler.getLock method."+ e);
            return false;
        }
    }

    /**
     * 释放锁
     * @param lockKey 锁的名字
     */
    public void realseLock(String lockKey) {
        if(!checkIfLockTimeout(System.currentTimeMillis(), lockKey)){
            jedis.del(lockKey);
        }
    }

    /**
     * 内部获取锁的实现方法
     * @param lockKey 锁的名字
     * @return
     */
    private boolean innerTryLock(String lockKey) {

        long currentTime = System.currentTimeMillis();//当前时间
        String lockTimeDuration = String.valueOf(currentTime + Lock_Timeout + 1);//锁的持续时间
        Long result = jedis.setnx(lockKey, lockTimeDuration);

        if (result == 1) {
            return true;
        } else {
            if (checkIfLockTimeout(currentTime, lockKey)) {
                String preLockTimeDuration = jedis.getSet(lockKey, lockTimeDuration);
                if (currentTime > Long.valueOf(preLockTimeDuration)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 判断加锁是否超时
     * @param currentTime 当前时间
     * @param lockKey 锁的名字
     * 比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，
     *   则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理
     * @return
     */
    private boolean checkIfLockTimeout(Long currentTime, String lockKey) {
        String time = "0";
        if(jedis.get(lockKey)!= null){
            time = jedis.get(lockKey);
        }
        if (currentTime > Long.parseLong(time)) {//当前时间超过锁的持续时间
            return true;
        } else {
            return false;
        }
    }

    public DistributedLockHandler setJedis(Jedis jedis) {
        this.jedis = jedis;
        return this;
    }

}
```

```java
// 调用
public static void main(String[] args) { 
	Jedis jedis = new Jedis(redisHost, redisPort);
        DistributedLockHandler distributedLockHandler = new DistributedLockHandler().setJedis(jedis);
        try {
            boolean getLock = distributedLockHandler.tryLock(lockKey, 5L);
            if (getLock) {
                // Do your job  
            System.out.println("Do your job........");  
        }  

    }catch(Exception e){  
        System.out.println(e);  
    }finally {  
        distributedLockHandler.realseLock(lockKey);  
    }  
}
```

