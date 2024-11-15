package com.fourback.redisstudy.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class ItemInquiryResponseDto {
    private String name;
    private String description;
    private String imageUrl;
    private LocalDate createAt;
    private LocalDate endingAt;
    private String ownerId;
    private String highestBidUserId;
    private String status;
    private Double price;
    private Integer views;
    private Integer likes;
    private Integer bids;

    public static ItemInquiryResponseDto from(Map<String, String> map) {
        return ItemInquiryResponseDto.builder()
                .name(map.get("name"))
                .description(map.get("description"))
                .imageUrl(map.get("imageUrl"))
                .createAt(LocalDate.parse(map.get("createAt")))
                .endingAt(LocalDate.parse(map.get("endingAt")))
                .ownerId(map.get("ownerId"))
                .highestBidUserId(map.get("highestBidUserId"))
                .status(map.get("status"))
                .price(Double.parseDouble(map.get("price")))
                .views(Integer.parseInt(map.get("views")))
                .likes(Integer.parseInt(map.get("likes")))
                .bids(Integer.parseInt(map.get("bids")))
                .build();
    }
}
