package com.hcc.common.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.hcc.common.model.bo.UserInfoBO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * Description: redis配置
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Configuration
public class RedisConfig {

    /**
     * 设置Redis序列化方式，默认使用的JDKSerializer的序列化方式，效率低，改用 FastJsonRedisSerializer
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Long> longRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        // key序列化
        redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(String.class));
        // value序列化
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Long.class));
        // Hash key序列化
        redisTemplate.setHashKeySerializer(new FastJsonRedisSerializer<>(String.class));
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(String.class));
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    /**
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化
        redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(String.class));
        // value序列化
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(UserInfoBO.class));
        // Hash key序列化
        redisTemplate.setHashKeySerializer(new FastJsonRedisSerializer<>(String.class));
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(String.class));
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
