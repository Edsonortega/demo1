package com.example.service;

import com.example.entity.Student;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Student> redisTemplate;

    public RedisService(RedisTemplate<String, Student> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setStudent(String key, Student s) {
        redisTemplate.opsForValue().set(key, s);
    }

    public void setStudentWithExpiration(String key, Student s, long expiration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, s, expiration, unit);
    }

    public Student getStudent(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteStudent(String key) {
        redisTemplate.delete(key);
    }
}
