package com.mmc.bookduck.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    public void setValuesWithTimeout(String key, String value, Duration timeout){
        redisTemplate.opsForValue().set(key,value,timeout);
    }

    public boolean setValuesWithTimeoutIfAbsent(String key, String value, Duration timeout){
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    @Transactional(readOnly = true)
    public Object getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValues(String key){
        redisTemplate.delete(key);
    }

}
