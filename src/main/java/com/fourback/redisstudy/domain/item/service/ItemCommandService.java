package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.repository.ItemRepository;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.CommonRepository;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemCommandService {
    private final ItemRepository itemRepository;
    private final CommonRepository commonRepository;


    public ItemCreateResponseDto create(ItemCreateRequestDto createRequestDto) {
        String itemId = RandomUtil.genId();

        itemRepository.setupForCreateItem(itemId, createRequestDto.toMap(), createRequestDto.getEndingAt());

        return ItemCreateResponseDto.from(itemId);
    }

    public void like(String userId, String itemId) {
        Boolean inserted = commonRepository.sAdd(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        if (inserted) {
            commonRepository.hIncrBy(PrefixEnum.ITEM.getPrefix() + itemId, "likes", 1);
        }
    }

    public void unlike(String userId, String itemId) {
        Boolean removed = commonRepository.sRem(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        if (removed) {
            commonRepository.hIncrBy(PrefixEnum.ITEM.getPrefix() + itemId, "likes", -1);
        }
    }

    public void create(String amount, String userId, String itemId) {
        Map<String, String> itemInquiryMap = commonRepository.hGetAll(PrefixEnum.ITEM.getPrefix() + itemId);
        if(itemInquiryMap.get("ownerId")==null){
            throw new RuntimeException("저장되지 않은 아이템");
        }
        if(Double.parseDouble(itemInquiryMap.get("price"))>=Double.parseDouble(amount)){
            throw new RuntimeException("현재 경매가보다 낮음");
        }
        if(LocalDate.parse(itemInquiryMap.get("endingAt")).isBefore(LocalDate.now())){
            throw new RuntimeException("경매가 끝난 아이템");
        }

        Map<String, String> info = new HashMap<>();
        info.put("bids", itemInquiryMap.get("bids")+1);
        info.put("price", amount);
        info.put("ownerId", userId);


        commonRepository.hSet(PrefixEnum.ITEM.getPrefix() + itemId, info);
        commonRepository.rPush(PrefixEnum.HISTORY.getPrefix() + itemId, amount);
    }
}
