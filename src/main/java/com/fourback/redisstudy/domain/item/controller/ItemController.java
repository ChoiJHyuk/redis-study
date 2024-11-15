package com.fourback.redisstudy.domain.item.controller;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemDetailResponseDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemInquiryResponseDto;
import com.fourback.redisstudy.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemCreateRequestDto createRequestDto) {
        ItemCreateResponseDto createResponseDto = itemService.create(createRequestDto);

        return ResponseEntity.ok(createResponseDto);
    }

    @GetMapping("/{user-id}/{item-id}")
    public ResponseEntity<?> getItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        ItemDetailResponseDto inquiryResponseDto = itemService.get(userId, itemId);

        return ResponseEntity.ok(inquiryResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> getSomeItems(@RequestBody List<String> itemIds) {
        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = itemService.getSome(itemIds);

        return ResponseEntity.ok(itemInquiryResponseDtoList);
    }

    @PostMapping("/{user-id}/like/{item-id}")
    public ResponseEntity<?> likeItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        itemService.like(userId, itemId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{user-id}/unlike/{item-id}")
    public ResponseEntity<?> unlikeItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        itemService.unlike(userId, itemId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{user-id}/liked")
    public ResponseEntity<?> getSomeItems(@PathVariable("user-id") String userId) {
        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = itemService.getSome(userId);

        return ResponseEntity.ok(itemInquiryResponseDtoList);
    }
}
