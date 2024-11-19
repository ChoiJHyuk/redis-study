package com.fourback.redisstudy.domain.item.repository;

import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final DefaultRedisScript<Void> addOneAndStoreScript;
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

    public List<String> sort(String key, Long offset) {
        return redisTemplate.sort(SortQueryBuilder.sort(key)
                .noSort()
                .get(PrefixEnum.ITEM.getPrefix() + "*->name")
                .get(PrefixEnum.ITEM.getPrefix() + "*->description")
                .get(PrefixEnum.ITEM.getPrefix() + "*->imageUrl")
                .get(PrefixEnum.ITEM.getPrefix() + "*->createAt")
                .get(PrefixEnum.ITEM.getPrefix() + "*->endingAt")
                .get(PrefixEnum.ITEM.getPrefix() + "*->ownerId")
                .get(PrefixEnum.ITEM.getPrefix() + "*->highestBidUserId")
                .get(PrefixEnum.ITEM.getPrefix() + "*->status")
                .get(PrefixEnum.ITEM.getPrefix() + "*->price")
                .get(PrefixEnum.ITEM.getPrefix() + "*->views")
                .get(PrefixEnum.ITEM.getPrefix() + "*->likes")
                .get(PrefixEnum.ITEM.getPrefix() + "*->bids")
                .order(SortParameters.Order.DESC)
                .limit(offset, 10)
                .build());
    }

    public void addOneAndStore(List<String> key, String[] value) {
        redisTemplate.execute(addOneAndStoreScript, key, value);
    }
}
