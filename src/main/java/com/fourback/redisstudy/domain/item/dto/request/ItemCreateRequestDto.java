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
    private Integer bids;
    private Double price;
    private Integer views;
    private Integer likes;
    private String status;
    private String ownerId;
    private String imageUrl;
    private LocalDate createAt;
    private LocalDate endingAt;
    private String description;
    private String highestBidUserId;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("bids", bids.toString());
        map.put("price", price.toString());
        map.put("views", views.toString());
        map.put("likes", likes.toString());
        map.put("status", status);
        map.put("ownerId", ownerId);
        map.put("imageUrl", imageUrl);
        map.put("createAt", createAt.toString());
        map.put("endingAt", endingAt.toString());
        map.put("description", description);
        map.put("highestBidUserId", highestBidUserId);

        return map;
    }
}
