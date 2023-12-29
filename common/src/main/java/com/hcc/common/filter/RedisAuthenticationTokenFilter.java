package com.hcc.common.filter;

import com.hcc.common.component.RedisComponent;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.model.bo.UserInfoBO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
public class RedisAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisComponent redisComponent;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 如果请求未携带token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 如果请求携带内部访问token
        if (token.equals(CustomConstants.InnerToken.TOKEN)){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("innerVisit", null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 通过token获取缓存的用户信息认证 并更新token过期时间
        UserInfoBO userInfoBO = (UserInfoBO) redisComponent.getObject(token);
        if (userInfoBO == null) {
            filterChain.doFilter(request, response);
            return;
        }
        redisComponent.expireForObject(token, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(userInfoBO.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfoBO, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
