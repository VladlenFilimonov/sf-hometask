package com.sunfinance.hometask.verification.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ValueOperations<String, VerificationCreateEvent> verificationCreateEventValueOperations(RedisConnectionFactory connectionFactory,
                                                                                                   ObjectMapper objectMapper) {
        return buildRedisTemplate(connectionFactory, objectMapper, VerificationCreateEvent.class);
    }

    private <K, V> ValueOperations<K, V> buildRedisTemplate(RedisConnectionFactory connectionFactory,
                                                            ObjectMapper objectMapper,
                                                            Class<V> val) {
        var json = new Jackson2JsonRedisSerializer<>(val);
        json.setObjectMapper(objectMapper);
        var string = RedisSerializer.string();
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(json);
        redisTemplate.setKeySerializer(string);
        redisTemplate.setHashValueSerializer(json);
        redisTemplate.setHashKeySerializer(string);
        redisTemplate.afterPropertiesSet();
        return redisTemplate.opsForValue();
    }
}
