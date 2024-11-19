package com.fourback.redisstudy.domain.user.repository;

import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final StringRedisTemplate redisTemplate;

    public void setupForCreateUser(String userId, Map<String, String> createRequestMap) {
        String userKey = PrefixEnum.USER.getPrefix() + userId;
        String username = createRequestMap.get("username");

        Map<byte[], byte[]> byteCreateRequestMap = createRequestMap.entrySet().stream().collect(
                Collectors.toMap(entry -> entry.getKey().getBytes(), entry -> entry.getValue().getBytes()));

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.hashCommands().hMSet(userKey.getBytes(), byteCreateRequestMap);
            connection.zSetCommands().zAdd(
                    PrefixEnum.USERNAME.getPrefix().getBytes(), Integer.parseInt(userId, 16), username.getBytes());
            return null;
        });
    }
}