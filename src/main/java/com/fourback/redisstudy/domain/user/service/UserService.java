package com.fourback.redisstudy.domain.user.service;

import com.fourback.redisstudy.domain.user.dto.request.UserCreateRequestDto;
import com.fourback.redisstudy.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.redisstudy.domain.user.dto.response.UserCreateResponseDto;
import com.fourback.redisstudy.domain.user.dto.response.UserInquiryResponseDto;
import com.fourback.redisstudy.domain.user.dto.response.UserLoginResponseDto;
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

        String username = createRequestDto.getUsername();

        if (redisRepository.sIsMember(PrefixEnum.UNIQUE_USER.getPrefix(), username)) {
            throw new RuntimeException("중복된 아이디");
        }

        String encryptPassword = EncryptionUtil.encryptPassword(createRequestDto.getPassword());
        createRequestDto.setPassword(encryptPassword);

        redisRepository.hSet(PrefixEnum.USER.getPrefix() + userId, createRequestDto.toMap());
        redisRepository.sAdd(PrefixEnum.UNIQUE_USER.getPrefix(), username);
        redisRepository.zAdd(PrefixEnum.USERNAME.getPrefix(), username, Integer.parseInt(userId, 16));

        return UserCreateResponseDto.from(userId);
    }

    public UserInquiryResponseDto get(String userId) {
        Map<String, String> inquiryMap = redisRepository.hGetAll(PrefixEnum.USER.getPrefix() + userId);

        return UserInquiryResponseDto.from(inquiryMap);
    }

    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto) {
        Double decimalId = redisRepository.zScore(PrefixEnum.USERNAME.getPrefix(), loginRequestDto.getUsername());
        if (decimalId == null) {
            throw new RuntimeException("존재하지 않은 아이디");
        }

        String userId = Integer.toHexString(decimalId.intValue());

        Map<String, String> userInquiryMap = redisRepository.hGetAll(PrefixEnum.USER.getPrefix() + userId);
        String hashedPassword = userInquiryMap.get("password");

        if (!EncryptionUtil.checkPassword(loginRequestDto.getPassword(), hashedPassword)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return UserLoginResponseDto.from(userId);
    }
}
