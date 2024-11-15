package com.fourback.redisstudy.domain.item.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ItemCreateRequestDto {
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

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("imageUrl", imageUrl);
        map.put("createAt", createAt.toString());
        map.put("endingAt", endingAt.toString());
        map.put("ownerId", ownerId);
        map.put("highestBidUserId", highestBidUserId);
        map.put("status", status);
        map.put("price", price.toString());
        map.put("views", views.toString());
        map.put("likes", likes.toString());
        map.put("bids", bids.toString());

        return map;
    }
}
