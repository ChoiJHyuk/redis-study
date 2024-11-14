package com.fourback.redisstudy.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UserCreateRequestDto {
    private String username;
    private String password;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        return map;
    }
}
