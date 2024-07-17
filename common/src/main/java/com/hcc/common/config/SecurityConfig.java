package com.hcc.common.config;


import com.hcc.common.filter.RedisAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Description: SpringSecurity配置
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    RedisAuthenticationTokenFilter redisAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.requestCache(RequestCacheConfigurer::disable);
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/auth/login").anonymous()
                        .requestMatchers("/auth/register").anonymous()
                        .requestMatchers("/event/listEvent").permitAll()
                        .requestMatchers("/task/updateScore").permitAll()
                        .requestMatchers("/team/listTeamByParadigm").permitAll()
                        .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(redisAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
