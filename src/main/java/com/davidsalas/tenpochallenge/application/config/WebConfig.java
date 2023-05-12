package com.davidsalas.tenpochallenge.application.config;

import com.davidsalas.tenpochallenge.application.aop.RateLimitingInterceptor;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RedissonClient redissonClient;

    public WebConfig(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitingInterceptor(redissonClient, "api-rate-limit", 3));
    }
}
