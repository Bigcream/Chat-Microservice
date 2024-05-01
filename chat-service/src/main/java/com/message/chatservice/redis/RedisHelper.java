package com.message.chatservice.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisHelper {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EMPTY_STRING = "";
    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    public void save(String key, Object object){
        redisTemplate.opsForValue().set(key, object);
    }

    public <T> T findByKey(String key, final Class<T> valueType){
        Object object = redisTemplate.opsForValue().get(key);
        if(object != null){
            try {
                return objectMapper.readValue(object.toString(), valueType);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return null;
    }

    public String findByKey(String key){
        Object object = redisTemplate.opsForValue().get(key);
        if(object != null){
            return object.toString();
        }
        return EMPTY_STRING;
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public List<Object> findAllByKeyPattern(String keyPattern){
        Set<String> keys = redisTemplate.keys(keyPattern);
        assert keys != null;
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
