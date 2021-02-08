package cn.com.bbut.iy.itemmaster.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.bbut.iy.itemmaster.util.IyCacheKeyGenerator;

/**
 * redis及cache配置
 * 
 * @author shiy
 *
 */
@Configuration
@EnableRedisHttpSession
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private SessionRedisConfig sessionRedisConfig;

    @Autowired
    private CacheRedisConfig cacheRedisConfig;

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        return buildTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new IyCacheKeyGenerator();
    }

    @Bean
    @Primary
    @ConditionalOnBean(value = CacheRedisConfig.class)
    public LettuceConnectionFactory cacheLettuceConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(cacheRedisConfig.getTimeout()))
                .poolConfig(cacheRedisConfig.poolConfig()).build();
        return new LettuceConnectionFactory(cacheRedisConfig.redisConfig(), clientConfig);
    }

    @Bean
    @SpringSessionRedisConnectionFactory
    @ConditionalOnBean(value = SessionRedisConfig.class)
    public LettuceConnectionFactory sessionLettuceConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(sessionRedisConfig.getTimeout()))
                .poolConfig(sessionRedisConfig.poolConfig()).build();
        return new LettuceConnectionFactory(sessionRedisConfig.redisConfig(), clientConfig);
    }

    @Bean(name = "sessionRedisTemplate")
    @ConditionalOnBean(name = "sessionLettuceConnectionFactory")
    public RedisTemplate<String, Object> sessionRedisTemplate(
            LettuceConnectionFactory sessionLettuceConnectionFactory) {
        return buildTemplate(sessionLettuceConnectionFactory);
    }

    private RedisTemplate<String, Object> buildTemplate(RedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}