package me.toddydev.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private RedisTemplate<String, Object> template = new RedisTemplate<>();

    public RedisService() {
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
    }

    public void set(String key, Object value, int timeout) {
        template.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    public String get(String key) {
        return (String) template.opsForValue().get(key);
    }

}
