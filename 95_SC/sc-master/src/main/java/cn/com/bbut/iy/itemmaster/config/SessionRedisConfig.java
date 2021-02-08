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
@ConditionalOnProperty(name = "host", prefix = "iy.session-redis")
public class SessionRedisConfig {

    @Value("${iy.session-redis.host:127.0.0.1}")
    /** redis host */
    private String host;
    @Value("${iy.session-redis.port:6379}")
    /** redis port */
    private Integer port;
    @Value("${iy.session-redis.password:123456}")
    /** redis password */
    private String password;
    @Value("${iy.session-redis.database:0}")
    /** redis database */
    private Integer database;
    @Value("${iy.session-redis.timeout:100}")
    /** redis timeout in ms */
    private Integer timeout;

    @Value("${iy.session-redis.lettuce.pool.max-active:8}")
    /** redis connection pool max active */
    private Integer maxActive;
    @Value("${iy.session-redis.lettuce.pool.max-idle:8}")
    private Integer maxIdle;
    @Value("${iy.session-redis.lettuce.pool.max-wait:-1}")
    private Long maxWait;
    @Value("${iy.session-redis.lettuce.pool.min-idle:0}")
    private Integer minIdle;

    public GenericObjectPoolConfig poolConfig() {
        return CacheRedisConfig.getGenericObjectPoolConfig(maxActive, maxIdle, minIdle, maxWait);
    }

    public RedisStandaloneConfiguration redisConfig() {
        return CacheRedisConfig.getRedisStandaloneConfiguration(host, password, port, database);
    }

}
