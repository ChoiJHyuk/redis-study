package com.fourback.redisstudy.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemCreateResponseDto {
    private String itemId;

    public static ItemCreateResponseDto from(String itemId) {
        return ItemCreateResponseDto.builder()
                .itemId(itemId)
                .build();
    }
}
