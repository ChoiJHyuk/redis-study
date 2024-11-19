package com.fourback.redisstudy.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class ItemDetailResponseDto {
    private String name;
    private Integer bids;
    private Double price;
    private Integer views;
    private Integer likes;
    private String status;
    private String ownerId;
    private String imageUrl;
    private Boolean isLiked;
    private LocalDate createAt;
    private LocalDate endingAt;
    private String description;
    private String highestBidUserId;

    public static ItemDetailResponseDto of(Map<String, String> map, Boolean isLiked) {
        return ItemDetailResponseDto.builder()
                .name(map.get("name"))
                .bids(Integer.parseInt(map.get("bids")))
                .price(Double.parseDouble(map.get("price")))
                .views(Integer.parseInt(map.get("views")))
                .likes(Integer.parseInt(map.get("likes")))
                .status(map.get("status"))
                .ownerId(map.get("ownerId"))
                .imageUrl(map.get("imageUrl"))
                .isLiked(isLiked)
                .createAt(LocalDate.parse(map.get("createAt")))
                .endingAt(LocalDate.parse(map.get("endingAt")))
                .description(map.get("description"))
                .highestBidUserId(map.get("highestBidUserId"))
                .build();
    }
}
