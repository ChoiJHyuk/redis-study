package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.RedisRepository;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final RedisRepository redisRepository;


    public ItemCreateResponseDto create(ItemCreateRequestDto createRequestDto) {
        String itemId = RandomUtil.genId();

        redisRepository.hSet(PrefixEnum.ITEM.getPrefix() + itemId, createRequestDto.toMap());

        return ItemCreateResponseDto.from(itemId);
    }

    public ItemDetailResponseDto get(String userId, String itemId) {
        Map<String, String> inquiryMap = redisRepository.hGetAll(PrefixEnum.ITEM.getPrefix() + itemId);

        Boolean isLiked = redisRepository.sIsMember(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        return ItemDetailResponseDto.of(inquiryMap, isLiked);
    }

    public List<ItemInquiryResponseDto> getSome(List<String> itemIds) {
        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = redisRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
    }

    public List<ItemInquiryResponseDto> getSome(String userId) {
        Set<String> itemIds = redisRepository.sMembers(PrefixEnum.USER_LIKE.getPrefix() + userId);

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = redisRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
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
