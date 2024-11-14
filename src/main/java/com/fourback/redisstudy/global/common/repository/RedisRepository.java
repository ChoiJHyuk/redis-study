package com.fourback.redisstudy.global.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;

    public void hSet(String key, Map<String, String> fields) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.putAll(key, fields);
    }

    public Map<String, String> hGetAll(String key) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        return hashOperations.entries(key);
    }
}
