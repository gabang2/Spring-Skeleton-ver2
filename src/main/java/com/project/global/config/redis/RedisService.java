package com.project.global.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T getRedisValue(String key, Class<T> classType) {
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if(ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(redisValue, classType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void putRedis(String key, Object classType){
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(classType), 14, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}