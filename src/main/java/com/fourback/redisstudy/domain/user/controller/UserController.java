package com.fourback.redisstudy.domain.user.controller;

import com.fourback.redisstudy.domain.user.dto.request.UserCreateRequestDto;
import com.fourback.redisstudy.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.redisstudy.domain.user.dto.response.UserCreateResponseDto;
import com.fourback.redisstudy.domain.user.dto.response.UserInquiryResponseDto;
import com.fourback.redisstudy.domain.user.dto.response.UserLoginResponseDto;
import com.fourback.redisstudy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDto createRequestDto) {
        UserCreateResponseDto createResponseDto = userService.create(createRequestDto);

        return ResponseEntity.ok(createResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto loginRequestDto) {
        UserLoginResponseDto loginResponseDto = userService.login(loginRequestDto);

        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<?> getUser(@PathVariable("user-id") String userId) {
        UserInquiryResponseDto inquiryResponseDto = userService.get(userId);

        return ResponseEntity.ok(inquiryResponseDto);
    }
}
