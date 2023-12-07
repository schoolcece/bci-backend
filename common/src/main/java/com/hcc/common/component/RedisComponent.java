package com.hcc.common.component;

import com.hcc.common.model.bo.UserInfoBO;
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

    public void expireForString(String key, long timeout, TimeUnit timeUnit) {
        objectRedisTemplate.expire(key, timeout, timeUnit);
    }

    public void expireForObject(String key, long timeout, TimeUnit timeUnit) {
        objectRedisTemplate.expire(key, timeout, timeUnit);
    }

    public void deleteForObject(String key) {
        Boolean delete = objectRedisTemplate.delete(key);
        System.out.println(delete);
    }
}
