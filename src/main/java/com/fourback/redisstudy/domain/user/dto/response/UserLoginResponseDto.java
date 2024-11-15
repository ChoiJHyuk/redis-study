package com.fourback.redisstudy.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String userId;

    public static UserLoginResponseDto from(String userId) {
        return UserLoginResponseDto.builder()
                .userId(userId)
                .build();
    }
}
