package com.fourback.redisstudy.domain.item.service;

import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.domain.item.repository.ItemRepository;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.CommonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemQueryService {
    private final CommonRepository commonRepository;
    private final ItemRepository itemRepository;

    public ItemDetailResponseDto get(String userId, String itemId) {
        itemRepository.addOneAndStore(List.of(PrefixEnum.ITEM.getPrefix() + itemId,
                        PrefixEnum.ITEM_VIEW.getPrefix(), PrefixEnum.VIEW.getPrefix() + itemId),
                new String[]{itemId, userId});

        Map<String, String> inquiryMap = commonRepository.hGetAll(PrefixEnum.ITEM.getPrefix() + itemId);

        Boolean isLiked = commonRepository.sIsMember(PrefixEnum.USER_LIKE.getPrefix() + userId, itemId);

        return ItemDetailResponseDto.of(inquiryMap, isLiked);
    }

    public List<ItemInquiryResponseDto> getSome(String userId) {
        List<String> itemIds = commonRepository.sMembers(PrefixEnum.USER_LIKE.getPrefix() + userId).stream().toList();

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = new ArrayList<>();

        for (int i = 0; i < itemIds.size(); i++) {
            itemInquiryResponseDtoList.add(ItemInquiryResponseDto.of(itemIds.get(i), inquiryMaps.get(i)));
        }

        return itemInquiryResponseDtoList;
    }

    public List<ItemInquiryResponseDto> getSome(String userId, String anotherUserId) {
        List<String> itemIds = commonRepository.sInter(PrefixEnum.USER_LIKE.getPrefix() + userId,
                PrefixEnum.USER_LIKE.getPrefix() + anotherUserId).stream().toList();

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = new ArrayList<>();

        for (int i = 0; i < itemIds.size(); i++) {
            itemInquiryResponseDtoList.add(ItemInquiryResponseDto.of(itemIds.get(i), inquiryMaps.get(i)));
        }

        return itemInquiryResponseDtoList;
    }

    public List<ItemInquiryResponseDto> getSomeByEndingAt(Long lastEndAt) {
        List<String> itemIds = commonRepository.zRange(lastEndAt).stream().toList();

        List<String> keys = itemIds.stream().map(itemId -> PrefixEnum.ITEM.getPrefix() + itemId).toList();

        List<Map<String, String>> inquiryMaps = commonRepository.hGetAllFromKeys(keys);

        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = new ArrayList<>();

        for (int i = 0; i < itemIds.size(); i++) {
            itemInquiryResponseDtoList.add(ItemInquiryResponseDto.of(itemIds.get(i), inquiryMaps.get(i)));
        }

        return itemInquiryResponseDtoList;
    }

    public List<ItemInquiryResponseDto> getSomeByView(Long offset) {
        List<String> itemInfoList = itemRepository.queryItemOrderByView(PrefixEnum.ITEM_VIEW.getPrefix(), offset);

        int fieldCount = ItemInquiryResponseDto.class.getDeclaredFields().length;

        List<ItemInquiryResponseDto> inquiryResponseDtoList = new ArrayList<>();

        for (int i = 0; i < itemInfoList.size(); i += fieldCount) {
            int end = Math.min(i + fieldCount, itemInfoList.size());
            List<String> subList = itemInfoList.subList(i, end);

            inquiryResponseDtoList.add(ItemInquiryResponseDto.from(subList));
        }

        return inquiryResponseDtoList;
    }

    public List<String> getBid(String itemId, Long offset) {
        long start = -1 * offset - 10;
        long end = -1 - offset;
        return commonRepository.lRange(PrefixEnum.HISTORY.getPrefix() + itemId, start, end);
    }
}
