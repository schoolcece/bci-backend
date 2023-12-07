package com.hcc.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Description: bci用户相关配置
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
public interface BCIConfig {

    @Configuration
    @ConfigurationProperties("bci.token")
    @Data
    class TokenConfig {
        private long timeout;
        private TimeUnit timeUnit;
    }


}
