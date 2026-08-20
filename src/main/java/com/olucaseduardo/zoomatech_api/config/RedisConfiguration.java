package com.olucaseduardo.zoomatech_api.config;

import com.olucaseduardo.zoomatech_api.util.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final CacheProperties cacheProperties;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        var configuration = new RedisStandaloneConfiguration();
        if (cacheProperties.getHost() != null && !cacheProperties.getHost().isBlank()) {
            configuration.setHostName(cacheProperties.getHost());
        }
        if (cacheProperties.getPort() > 0) {
            configuration.setPort(cacheProperties.getPort());
        }
        if (cacheProperties.getPassword() != null && !cacheProperties.getPassword().isBlank()) {
            configuration.setPassword(cacheProperties.getPassword());
        }
        return configuration;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(30))
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}