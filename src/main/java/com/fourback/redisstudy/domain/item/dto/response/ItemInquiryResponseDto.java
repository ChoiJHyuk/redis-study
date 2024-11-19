package com.fourback.redisstudy.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ItemInquiryResponseDto {
    private String name;
    private Integer bids;
    private Double price;
    private String itemId;
    private Integer views;
    private Integer likes;
    private String status;
    private String ownerId;
    private String imageUrl;
    private LocalDate createAt;
    private LocalDate endingAt;
    private String description;
    private String highestBidUserId;

    public static ItemInquiryResponseDto of(String itemId, Map<String, String> map) {
        return ItemInquiryResponseDto.builder()
                .name(map.get("name"))
                .bids(Integer.parseInt(map.get("bids")))
                .price(Double.parseDouble(map.get("price")))
                .itemId(itemId)
                .views(Integer.parseInt(map.get("views")))
                .likes(Integer.parseInt(map.get("likes")))
                .status(map.get("status"))
                .ownerId(map.get("ownerId"))
                .imageUrl(map.get("imageUrl"))
                .createAt(LocalDate.parse(map.get("createAt")))
                .endingAt(LocalDate.parse(map.get("endingAt")))
                .description(map.get("description"))
                .highestBidUserId(map.get("highestBidUserId"))
                .build();
    }

    public static ItemInquiryResponseDto from(List<String> list){
        return ItemInquiryResponseDto.builder()
                .name(list.get(0))
                .bids(Integer.parseInt(list.get(1)))
                .price(Double.parseDouble(list.get(2)))
                .itemId(list.get(3))
                .views(Integer.parseInt(list.get(4)))
                .likes(Integer.parseInt(list.get(5)))
                .status(list.get(6))
                .ownerId(list.get(7))
                .imageUrl(list.get(8))
                .createAt(LocalDate.parse(list.get(9)))
                .endingAt(LocalDate.parse(list.get(10)))
                .description(list.get(11))
                .highestBidUserId(list.get(12))
                .build();
    }
}
