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

    @GetMapping("/{user-id}/{item-id}")
    public ResponseEntity<?> getItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        ItemDetailResponseDto inquiryResponseDto = itemQueryService.get(userId, itemId);

        return ResponseEntity.ok(inquiryResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> getSomeItems(@RequestBody List<String> itemIds) {
        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = itemQueryService.getSome(itemIds);

        return ResponseEntity.ok(itemInquiryResponseDtoList);
    }

    @GetMapping("/{user-id}/liked")
    public ResponseEntity<?> getSomeItems(@PathVariable("user-id") String userId) {
        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = itemQueryService.getSome(userId);

        return ResponseEntity.ok(itemInquiryResponseDtoList);
    }
}
