package cn.com.bbut.iy.itemmaster.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author shiy
 */
@Component
@Data
@ConditionalOnProperty(name = "host", prefix = "iy.cache-redis")
public class CacheRedisConfig {

    @Value("${iy.cache-redis.host:127.0.0.1}")
    /** redis host */
    private String host;
    @Value("${iy.cache-redis.port:6379}")
    /** redis port */
    private Integer port;
    @Value("${iy.cache-redis.password:123456}")
    /** redis password */
    private String password;
    @Value("${iy.cache-redis.database:0}")
    /** redis database */
    private Integer database;
    @Value("${iy.cache-redis.timeout:100}")
    /** redis timeout in ms */
    private Integer timeout;

    @Value("${iy.cache-redis.lettuce.pool.max-active:8}")
    /** redis connection pool max active */
    private Integer maxActive;
    @Value("${iy.cache-redis.lettuce.pool.max-idle:8}")
    private Integer maxIdle;
    @Value("${iy.cache-redis.lettuce.pool.max-wait:-1}")
    private Long maxWait;
    @Value("${iy.cache-redis.lettuce.pool.min-idle:0}")
    private Integer minIdle;

    public GenericObjectPoolConfig poolConfig() {
        return getGenericObjectPoolConfig(maxActive, maxIdle, minIdle, maxWait);
    }

    static GenericObjectPoolConfig getGenericObjectPoolConfig(Integer maxActive, Integer maxIdle, Integer minIdle, Long maxWait) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        return config;
    }

    public RedisStandaloneConfiguration redisConfig() {
        return getRedisStandaloneConfiguration(host, password, port, database);
    }

    static RedisStandaloneConfiguration getRedisStandaloneConfiguration(String host, String password, Integer port, Integer database) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPassword(RedisPassword.of(password));
        config.setPort(port);
        config.setDatabase(database);
        return config;
    }

}
