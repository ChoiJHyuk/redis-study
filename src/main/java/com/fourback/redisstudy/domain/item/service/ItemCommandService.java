package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.repository.ItemRepository;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.CommonRepository;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void create(String amount, String itemId) {
        commonRepository.rPush(PrefixEnum.HISTORY.getPrefix() + itemId, amount);
    }
}
