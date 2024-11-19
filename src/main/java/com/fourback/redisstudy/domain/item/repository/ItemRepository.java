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

    public void setupForCreateItem(String itemId, Map<String, String> createRequestMap, LocalDate endingAt) {
        String key = PrefixEnum.ITEM.getPrefix() + itemId;

        Map<byte[], byte[]> byteCreateRequestMap = createRequestMap.entrySet().stream().collect(
                Collectors.toMap(entry -> entry.getKey().getBytes(), entry -> entry.getValue().getBytes()));

        long milliEndingAt = endingAt.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.hashCommands().hMSet(key.getBytes(), byteCreateRequestMap);
            connection.zSetCommands().zAdd(PrefixEnum.ITEM_VIEW.getPrefix().getBytes(), 0, itemId.getBytes());
            connection.zSetCommands().zAdd(PrefixEnum.ITEM_ENDING_AT.getPrefix().getBytes(), milliEndingAt, itemId.getBytes());
            return null;
        });
    }

    public List<String> queryItemOrderByView(String key, Long offset) {
        return redisTemplate.sort(SortQueryBuilder.sort(key)
                .noSort()
                .get(PrefixEnum.ITEM.getPrefix() + "*->name")
                .get(PrefixEnum.ITEM.getPrefix() + "*->bids")
                .get(PrefixEnum.ITEM.getPrefix() + "*->price")
                .get("#")
                .get(PrefixEnum.ITEM.getPrefix() + "*->views")
                .get(PrefixEnum.ITEM.getPrefix() + "*->likes")
                .get(PrefixEnum.ITEM.getPrefix() + "*->status")
                .get(PrefixEnum.ITEM.getPrefix() + "*->ownerId")
                .get(PrefixEnum.ITEM.getPrefix() + "*->imageUrl")
                .get(PrefixEnum.ITEM.getPrefix() + "*->createAt")
                .get(PrefixEnum.ITEM.getPrefix() + "*->endingAt")
                .get(PrefixEnum.ITEM.getPrefix() + "*->description")
                .get(PrefixEnum.ITEM.getPrefix() + "*->highestBidUserId")
                .order(SortParameters.Order.DESC)
                .limit(offset, 10)
                .build());
    }

    public void addOneAndStore(List<String> key, String[] value) {
        redisTemplate.execute(addOneAndStoreScript, key, (Object) value);
    }

    public void setupForBidItem(String itemId, Map<String, String> updateMap, String amount) {
        String itemKey = PrefixEnum.ITEM.getPrefix() + itemId;
        String historyKey = PrefixEnum.HISTORY.getPrefix() + itemId;

        Map<byte[], byte[]> byteUpdateMaptMap = updateMap.entrySet().stream().collect(
                Collectors.toMap(entry -> entry.getKey().getBytes(), entry -> entry.getValue().getBytes()));

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.hashCommands().hMSet(itemKey.getBytes(), byteUpdateMaptMap);
            connection.listCommands().rPush(historyKey.getBytes(), amount.getBytes());
            return null;
        });
    }
}
