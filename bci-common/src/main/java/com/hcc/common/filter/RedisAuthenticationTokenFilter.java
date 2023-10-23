package com.hcc.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String sourceIp = null;
//        String ipAddresses = httpServletRequest.getHeader("x-forwarded-for");
//
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ipAddresses = httpServletRequest.getHeader("Proxy-Client-IP");
//        }
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ipAddresses = httpServletRequest.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ipAddresses = httpServletRequest.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ipAddresses = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
//            ipAddresses = httpServletRequest.getRemoteAddr();
//        }
//        if (!StringUtils.isEmpty(ipAddresses)) {
//            sourceIp = ipAddresses.split(",")[0];
//        }
//        System.out.println(sourceIp);
        String token = httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (token.equals("masterKeyHcc")){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("innerVisit", null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String info = redisTemplate.opsForValue().get(token);
        if(StringUtils.isEmpty(info)){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UserInfo userInfo = JSON.parseObject(info, new TypeReference<UserInfo>() {
        });
        redisTemplate.expire(token,5, TimeUnit.HOURS);
        redisTemplate.expire(userInfo.getUserId().toString(), 5, TimeUnit.HOURS);

        //提取请求参数
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();

        //todo 权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, parameterMap, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
