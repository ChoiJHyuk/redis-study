package com.fourback.redisstudy.global.common.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void hSet(String key, Map<String, String> fields) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, fields);
    }

    public Map<String, String> hGetAll(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        return hashOperations.entries(key);
    }

    public List<Map<String, String>> hGetAllFromKeys(List<String> keys) {
        List<byte[]> byteKeys = keys.stream().map(String::getBytes).toList();

        List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            byteKeys.forEach(byteKey -> connection.hashCommands().hGetAll(byteKey));
            return null;
        });

        return objectMapper.convertValue(
                objects, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
        );
    }
}
