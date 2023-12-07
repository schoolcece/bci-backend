package com.hcc.common.aop;

import com.hcc.common.annotation.Loggable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 记录日志切面
 * todo: 接口超时报警 输出warn日志
 */
@Aspect
@Component
public class LoggableAspect {

    private final Logger logger = LoggerFactory.getLogger("切面统一日志记录");


    @Around("@annotation(loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        logger.info("{}方法开始执行", loggable.value());
        long time = 0;
        Object result = null;
        try {
            long start = System.currentTimeMillis();
            result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            time = end - start;
            logger.info("{}方法执行成功", loggable.value());
        } catch (Throwable e) {
            throw e;
        } finally {
            logger.info("{}方法执行完毕, 耗时： {}ms", loggable.value(), time);
        }
        return result;
    }
}
