package com.hcc.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

//@Configuration
//public class BciSessionConfig {
//    @Bean
//    public CookieSerializer cookieSerializer(){
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//
//        cookieSerializer.setCookieName("BCISESSION");
//
//        return cookieSerializer;
//    }
//
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
//        return new GenericJackson2JsonRedisSerializer();
//    }
//}
