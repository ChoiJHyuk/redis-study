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

    public static ItemInquiryResponseDto of(Map<String, String> map) {
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

    public static ItemInquiryResponseDto from(List<String> itemInfo){
        return ItemInquiryResponseDto.builder()
                .name(itemInfo.get(0))
                .description(itemInfo.get(1))
                .imageUrl(itemInfo.get(2))
                .createAt(LocalDate.parse(itemInfo.get(3)))
                .endingAt(LocalDate.parse(itemInfo.get(4)))
                .ownerId(itemInfo.get(5))
                .highestBidUserId(itemInfo.get(6))
                .status(itemInfo.get(7))
                .price(Double.parseDouble(itemInfo.get(8)))
                .views(Integer.parseInt(itemInfo.get(9)))
                .likes(Integer.parseInt(itemInfo.get(10)))
                .bids(Integer.parseInt(itemInfo.get(11)))
                .build();
    }
}
