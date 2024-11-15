package com.fourback.redisstudy.domain.item.controller;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
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

    @GetMapping("/{item-id}")
    public ResponseEntity<?> getItem(@PathVariable("item-id") String itemId) {
        ItemInquiryResponseDto inquiryResponseDto = itemService.get(itemId);

        return ResponseEntity.ok(inquiryResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> getSomeItems(@RequestBody List<String> itemIds) {
        List<ItemInquiryResponseDto> itemInquiryResponseDtoList = itemService.getSome(itemIds);

        return ResponseEntity.ok(itemInquiryResponseDtoList);
    }
}
