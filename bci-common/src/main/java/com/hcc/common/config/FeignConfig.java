package com.hcc.common.config;

import com.hcc.common.utils.TokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
//                    log.debug(request.getHeader("token"));
                    if (request != null) {
                        //同步请求头数据 主要同步token
                        String token = request.getHeader("token");
                        //给新请求同步的老请求token
                        template.header("token", token);
                    }
                }else{
                    //异步线程丢失请求信息，通过线程变量找回token
//                    String token = TokenHolder.getToken();

                    template.header("token", "masterKeyHcc");
//                    TokenHolder.removeToken();
                }
            }
        };
    }

}
