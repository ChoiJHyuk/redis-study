package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.RedisRepository;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCommandService {
    private final RedisRepository redisRepository;


    public ItemCreateResponseDto create(ItemCreateRequestDto createRequestDto) {
        String itemId = RandomUtil.genId();

        redisRepository.hSet(PrefixEnum.ITEM.getPrefix() + itemId, createRequestDto.toMap());

        return ItemCreateResponseDto.from(itemId);
    }

    public void like(String userId, String itemId) {
        Boolean inserted = redisRepository.sAdd(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        if (inserted) {
            redisRepository.hIncrBy(PrefixEnum.ITEM.getPrefix() + itemId, "likes", 1);
        }
    }

    public void unlike(String userId, String itemId) {
        Boolean removed = redisRepository.sRem(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        if (removed) {
            redisRepository.hIncrBy(PrefixEnum.ITEM.getPrefix() + itemId, "likes", -1);
        }
    }
}
