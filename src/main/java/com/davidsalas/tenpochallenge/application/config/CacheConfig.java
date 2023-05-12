package com.davidsalas.tenpochallenge.application.config;


import com.davidsalas.tenpochallenge.domain.Percentage;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig {

    @Value("${application.cache.expiry.time}")
    private long ttl;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(java.time.Duration.ofSeconds(ttl))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Percentage.class)))
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"  + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }
}

