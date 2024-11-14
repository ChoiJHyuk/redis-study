package com.fourback.redisstudy.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class UserInquiryResponseDto {
    private String username;

    public static UserInquiryResponseDto from(Map<String, String> map) {
        return UserInquiryResponseDto.builder()
                .username(map.get("username"))
                .build();
    }
}
