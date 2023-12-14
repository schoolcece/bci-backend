package com.hcc.common.config;

import com.hcc.common.constant.CustomConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Description: feign远程调用相关配置
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Slf4j
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                        //同步请求头数据 主要同步token
                        String token = request.getHeader("token");
                        //给新请求同步的老请求token
                        template.header("token", token);
                }else{
                    template.header("token", CustomConstants.InnerToken.TOKEN);
                }
            }
        };
    }
}
