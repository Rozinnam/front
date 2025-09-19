package com.ggiiig.featureflag;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class FeatureFlagService {
    private final RedisTemplate<String, Boolean> redisTemplate;
    private final Map<String, Boolean> localCache = new ConcurrentHashMap<>();
    private static final String ENABLE_ASYNC_FILE_SERVICE = "enable_async_file_service";

    public FeatureFlagService(@Qualifier("booleanRedisTemplate")RedisTemplate<String, Boolean> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
//    private static final String FLAG = "async_file_service:enable";

    public boolean isEnableAsyncFileServiceMode() {
        log.info("FeatureFlagService : isEnableAsyncFileServiceMode");

        return localCache.getOrDefault(ENABLE_ASYNC_FILE_SERVICE, true);
    }

    public void updateFlag(boolean enable) {
        log.info("FeatureFlagRestController : updateFlag = {}", enable);

        localCache.put(ENABLE_ASYNC_FILE_SERVICE, enable);
        redisTemplate.opsForValue().set(ENABLE_ASYNC_FILE_SERVICE, enable);
    }

    @Scheduled(cron = "0 0 */2 * * *")
    public void syncFeatureFlagsFromRedis() {
        Boolean enable_async_file_service_value = redisTemplate.opsForValue().get(ENABLE_ASYNC_FILE_SERVICE);

        if (Objects.isNull(enable_async_file_service_value)) {
            enable_async_file_service_value = true;
        }

        localCache.put(ENABLE_ASYNC_FILE_SERVICE, enable_async_file_service_value);
    }

    @PostConstruct
    public void init() {
        Boolean value = redisTemplate.opsForValue().get(ENABLE_ASYNC_FILE_SERVICE);
        localCache.put(ENABLE_ASYNC_FILE_SERVICE, Objects.requireNonNullElse(value, true));
    }
}
