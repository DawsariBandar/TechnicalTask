package com.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RateLimiterConfig rateLimiterConfig() {
        return new RateLimiterConfig();
    }
}
