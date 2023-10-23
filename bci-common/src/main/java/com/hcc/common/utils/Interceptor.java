//package com.hcc.common.utils;
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//@Component
//public class Interceptor {
//    @Bean
//    public RequestInterceptor requestInterceptor(){
//        return templete -> {
//            ServletRequestAttributes requestAttributes =
//                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if(requestAttributes != null){
//                HttpServletRequest request = requestAttributes.getRequest();
//                if(request == null){
//                    return ;
//                }else{
//                    String token = request.getHeader("token");
//                    templete.header("token",token);
//                }
//            }
//        };
//    }
//}
