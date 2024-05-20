package com.hcc.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
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

    @Configuration
    @ConfigurationProperties("bci.code")
    @Data
    class CodeConfig {
        private String url;
    }

    @Configuration
    @ConfigurationProperties("bci.task")
    @Data
    class TaskConfig {
        private Map<Integer, Long> maxTime;
        private Map<Integer, Integer> maxExec;
        private String cmd;
        private String updateScoreURl;
        private String codePath;
        private Map<Integer, String> dataPath;
    }


}
