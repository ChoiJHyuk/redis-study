package com.fourback.redisstudy.domain.user.service;

import com.fourback.redisstudy.domain.user.dto.request.UserCreateRequestDto;
import com.fourback.redisstudy.domain.user.dto.response.UserCreateResponseDto;
import com.fourback.redisstudy.domain.user.dto.response.UserInquiryResponseDto;
import com.fourback.redisstudy.global.common.enums.PrefixEnum;
import com.fourback.redisstudy.global.common.repository.RedisRepository;
import com.fourback.redisstudy.global.common.util.EncryptionUtil;
import com.fourback.redisstudy.global.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisRepository redisRepository;

    public UserCreateResponseDto create(UserCreateRequestDto createRequestDto) {
        String userId = RandomUtil.genId();

        String encryptPassword = EncryptionUtil.encryptPassword(createRequestDto.getPassword());
        createRequestDto.setPassword(encryptPassword);

        redisRepository.hSet(PrefixEnum.USER.getPrefix() + userId, createRequestDto.toMap());

        return UserCreateResponseDto.from(userId);
    }

    public UserInquiryResponseDto get(String userId) {
        Map<String, String> inquiryMap = redisRepository.hGetAll(PrefixEnum.USER.getPrefix() + userId);

        return UserInquiryResponseDto.from(inquiryMap);
    }
}
