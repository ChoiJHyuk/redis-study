package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.RedisRepository;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final RedisRepository redisRepository;


    public ItemCreateResponseDto create(ItemCreateRequestDto createRequestDto) {
        String itemId = RandomUtil.genId();

        redisRepository.hSet(PrefixEnum.ITEM.getPrefix() + itemId, createRequestDto.toMap());

        return ItemCreateResponseDto.from(itemId);
    }

    public ItemInquiryResponseDto get(String itemId) {
        Map<String, String> inquiryMap = redisRepository.hGetAll(PrefixEnum.ITEM.getPrefix() + itemId);

        return ItemInquiryResponseDto.from(inquiryMap);
    }

    public List<ItemInquiryResponseDto> getSome(List<String> itemIds) {
        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = redisRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::from).toList();
    }
}
