package com.fourback.redisstudy.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateResponseDto {
    private String userId;

    public static UserCreateResponseDto from(String userId) {
        return UserCreateResponseDto.builder()
                .userId(userId)
                .build();
    }
}
