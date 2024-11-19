package com.fourback.redisstudy.domain.item.controller;

import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.domain.item.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemQueryController {
    private final ItemQueryService itemQueryService;

    @GetMapping("/detail/{user-id}/{item-id}")
    public ResponseEntity<?> getItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        ItemDetailResponseDto inquiryResponseDto = itemQueryService.get(userId, itemId);

        return ResponseEntity.ok(inquiryResponseDto);
    }

    @GetMapping("/{user-id}/liked")
    public ResponseEntity<?> getSomeLikedItems(@PathVariable("user-id") String userId) {
        List<ItemInquiryResponseDto> inquiryResponseDtoList = itemQueryService.getSome(userId);

        return ResponseEntity.ok(inquiryResponseDtoList);
    }

    @GetMapping("/intersection/{user-id}/{another-user-id}")
    public ResponseEntity<?> getSomeIntersectionItems(@PathVariable("user-id") String userId,
                                                      @PathVariable("another-user-id") String anotherUserId) {
        List<ItemInquiryResponseDto> inquiryResponseDtoList = itemQueryService.getSome(userId, anotherUserId);

        return ResponseEntity.ok(inquiryResponseDtoList);
    }

    @GetMapping("/order-end")
    public ResponseEntity<?> getSomeItemsByEndOrder(@RequestParam(value = "last-end-at", required = false) Long lastEndAt) {
        List<ItemInquiryResponseDto> inquiryResponseDtoList = itemQueryService.getSome(lastEndAt);

        return ResponseEntity.ok(inquiryResponseDtoList);
    }

    @GetMapping("/order-view")
    public ResponseEntity<?> getSomeItemsByViewOrder(
            @RequestParam(name = "offset", required = false, defaultValue = "0") Long offset) {
        List<ItemInquiryResponseDto> inquiryResponseDtoList = itemQueryService.getSomeByOffset(offset);

        return ResponseEntity.ok(inquiryResponseDtoList);
    }

    @GetMapping("/bid/{item-id}")
    public ResponseEntity<?> getBid(@PathVariable("item-id") String itemId,
                                    @RequestParam(name = "offset", required = false, defaultValue = "0") Long offset) {
        List<String> bids = itemQueryService.getBid(itemId, offset);
        return ResponseEntity.ok(bids);
    }
}
