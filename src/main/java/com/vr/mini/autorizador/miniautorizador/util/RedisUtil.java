package com.vr.mini.autorizador.miniautorizador.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public void delete(String key) {
    redisTemplate.delete(key);
  }
}
