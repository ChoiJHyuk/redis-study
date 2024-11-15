package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.domain.item.repository.ItemRepository;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.CommonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemQueryService {
    private final CommonRepository commonRepository;
    private final ItemRepository itemRepository;

    public ItemDetailResponseDto get(String userId, String itemId) {
        itemRepository.incrementViewCount(itemId);

        Map<String, String> inquiryMap = commonRepository.hGetAll(PrefixEnum.ITEM.getPrefix() + itemId);

        Boolean isLiked = commonRepository.sIsMember(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);


        return ItemDetailResponseDto.of(inquiryMap, isLiked);
    }

    public List<ItemInquiryResponseDto> getSome(List<String> itemIds) {
        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
    }

    public List<ItemInquiryResponseDto> getSome(String userId) {
        Set<String> itemIds = commonRepository.sMembers(PrefixEnum.USER_LIKE.getPrefix() + userId);

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
    }

    public List<ItemInquiryResponseDto> getSome(String userId, String anotherUserId) {
        Set<String> itemIds = commonRepository.sInter(
                PrefixEnum.USER_LIKE.getPrefix() + userId, PrefixEnum.USER_LIKE.getPrefix() + anotherUserId);

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
    }

    public List<ItemInquiryResponseDto> getSome(Long lastEndAt) {
        Set<String> itemIds = commonRepository.zRange(lastEndAt);
        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        return inquiryMaps.stream().map(ItemInquiryResponseDto::of).toList();
    }
}
