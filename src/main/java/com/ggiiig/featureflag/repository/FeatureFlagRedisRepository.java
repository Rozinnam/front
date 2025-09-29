package com.ggiiig.featureflag.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class FeatureFlagRedisRepository {
    private final RedisTemplate<String, Boolean> redisTemplate;

    public FeatureFlagRedisRepository(@Qualifier("booleanRedisTemplate") RedisTemplate<String, Boolean> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<Boolean> safeGet(String key) {
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(key));
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 연결 실패 GET: {} (로컬 캐시 fallback)", key, e);
            return Optional.empty();
        }
    }

    public boolean safeSet(String key, boolean value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 연결 실패 SET: {} (로컬 캐시에만 반영)", key, e);
            return false;
        }

        return true;
    }
}
