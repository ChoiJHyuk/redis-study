package com.fourback.redisstudy.domain.item.repository;

import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void setupForCreateItem(String itemId, Map<String, String> itemAttr, LocalDate endginAt) {
        String key = PrefixEnum.ITEM.getPrefix() + itemId;

        Map<byte[], byte[]> collect = itemAttr.entrySet().stream().collect(
                Collectors.toMap(entry -> entry.getKey().getBytes(), entry -> entry.getValue().getBytes()));

        long epochMilli = endginAt.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.hashCommands().hMSet(key.getBytes(), collect);
            connection.zSetCommands().zAdd(PrefixEnum.ITEM_VIEW.getPrefix().getBytes(), 0, itemId.getBytes());
            connection.zSetCommands().zAdd(PrefixEnum.ITEM_ENDING_AT.getPrefix().getBytes(), epochMilli, itemId.getBytes());
            return null;
        });
    }
}
