package com.hcc.common.config;


import com.hcc.common.filter.RedisAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedisAuthenticationTokenFilter redisAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/login").anonymous()
//                .antMatchers("/auth/register").hasIpAddress("10.112.5.184")
                .antMatchers("/auth/register").anonymous()
                .antMatchers("/task/updateScore").anonymous()
//                .antMatchers("/code/updateCode").anonymous()
//                .antMatchers("/log/saveLog").anonymous()
//                .antMatchers("/ComputeNode/updateNode").anonymous()
                .anyRequest().authenticated();
        http.addFilterBefore(redisAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors();
    }
}
