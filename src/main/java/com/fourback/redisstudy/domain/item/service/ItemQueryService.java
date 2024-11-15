package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemQueryService {
    private final RedisRepository redisRepository;

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
}
