package com.fourback.redisstudy.domain.item.repository;

import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final StringRedisTemplate redisTemplate;

    public void incrementViewCount(String itemId) {
        String key = PrefixEnum.ITEM.getPrefix() + itemId;

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.hashCommands().hIncrBy(key.getBytes(), "views".getBytes(), 1L);
            connection.zSetCommands().zIncrBy(PrefixEnum.ITEM_VIEW.getPrefix().getBytes(), 1D, itemId.getBytes());
            return null;
        });
    }
}
