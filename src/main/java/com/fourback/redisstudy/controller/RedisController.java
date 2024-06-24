package com.fourback.redisstudy.controller;

import com.fourback.redisstudy.dto.DataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redis/add")
    public String addData(@RequestBody DataDto dataDto){
        //opsFor~~()메소드를 통해 받아온 인터페이스로부터 직렬화와 역직렬화로 데이터를 redis로 전송하거나 받아옴
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(dataDto.getKey(), dataDto.getValue());
        return "ok";
    }

    @GetMapping("/redis/get/{id}")
    public DataDto getData(@PathVariable String id){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(id);
        DataDto dataDto = new DataDto();
        dataDto.setKey(id);
        dataDto.setValue(value);
        return dataDto;
    }
}
