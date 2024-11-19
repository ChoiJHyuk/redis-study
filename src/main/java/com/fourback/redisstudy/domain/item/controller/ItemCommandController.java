package com.fourback.redisstudy.domain.item.controller;

import com.fourback.redisstudy.domain.item.dto.request.ItemCreateRequestDto;
import com.fourback.redisstudy.domain.item.dto.response.ItemCreateResponseDto;
import com.fourback.redisstudy.domain.item.service.ItemCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemCommandController {
    private final ItemCommandService itemCommandService;

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemCreateRequestDto createRequestDto) {
        ItemCreateResponseDto createResponseDto = itemCommandService.create(createRequestDto);

        return ResponseEntity.ok(createResponseDto);
    }

    @PostMapping("/{user-id}/like/{item-id}")
    public ResponseEntity<?> likeItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        itemCommandService.like(userId, itemId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{user-id}/unlike/{item-id}")
    public ResponseEntity<?> unlikeItem(@PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        itemCommandService.unlike(userId, itemId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{user-id}/bid/{item-id}")
    public ResponseEntity<?> bidItem(@RequestParam("amount") String amount,
                                     @PathVariable("user-id") String userId, @PathVariable("item-id") String itemId) {
        itemCommandService.bid(amount, userId, itemId);

        return ResponseEntity.ok().build();
    }
}
