package com.hcc.common.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Description: redis操作组件
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Component
public class RedisComponent {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<String, Object> objectRedisTemplate;

    @Autowired
    RedisTemplate<String, Long> longRedisTemplate;

    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setString(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void setObject(String key, Object value) {
        objectRedisTemplate.opsForValue().set(key, value);
    }

    public void setObject(String key, Object value, long timeout, TimeUnit timeUnit) {
        objectRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public Object getObject(String key) {
        return objectRedisTemplate.opsForValue().get(key);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Long getLong(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }

    public void expireForString(String key, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    public void expireForObject(String key, long timeout, TimeUnit timeUnit) {
        objectRedisTemplate.expire(key, timeout, timeUnit);
    }

    public void deleteForObject(String key) {
        Boolean delete = objectRedisTemplate.delete(key);
    }

    public void deleteForLong(String key) {
        Boolean delete = longRedisTemplate.delete(key);
    }

    public boolean setIfAbsent(String key, Long value, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(longRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit));
    }

    public Long getExpireForLong(String countKey, TimeUnit seconds) {
        return longRedisTemplate.getExpire(countKey, seconds);
    }

    public void increment(String countKey) {
        longRedisTemplate.opsForValue().increment(countKey);
    }
}
