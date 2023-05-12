package com.davidsalas.tenpochallenge.application.aop;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RedissonClient redissonClient;
    private final String limitMap;
    private final int permits;

    public RateLimitingInterceptor(RedissonClient redissonClient, String limitMap, int permits) {
        this.redissonClient = redissonClient;
        this.limitMap = limitMap;
        this.permits = permits;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getRequestURL().toString().contains("/calculations") && request.getMethod().equals(HttpMethod.GET.name()))
            return true;

        RMap<String, LocalDateTime> requestsMap = redissonClient.getMap(limitMap);
        requestsMap.values().removeIf(time -> ChronoUnit.HOURS.between(time, LocalDateTime.now()) >= 1);

        if (requestsMap.size() >= permits) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        } else {
            requestsMap.put(UUID.randomUUID().toString(), LocalDateTime.now());
            return true;
        }
    }
}
