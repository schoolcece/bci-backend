package com.hcc.task.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    /**
     * 设置Redis序列化方式，默认使用的JDKSerializer的序列化方式，效率低，这里我们使用 FastJsonRedisSerializer
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Long> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        // key序列化
        redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(Object.class));
        // value序列化
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Long.class));
//        // Hash key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        // Hash value序列化
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
