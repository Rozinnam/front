package com.ggiiig.featureflag;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagService {
    private final RedisTemplate<String, Boolean> redisTemplate;
    private final Map<String, Boolean> localCache = new ConcurrentHashMap<>();
    private static final String ENABLE_ASYNC_FILE_SERVICE = "enable_async_file_service";
//    private static final String FLAG = "async_file_service:enable";

    public boolean isEnableAsyncFileServiceMode() {
        log.info("FeatureFlagService : isEnableAsyncFileServiceMode");

        return localCache.getOrDefault(ENABLE_ASYNC_FILE_SERVICE, false);
    }

    public void updateFlag(boolean enable) {
        log.info("FeatureFlagRestController : updateFlag = {}", enable);

        localCache.put(ENABLE_ASYNC_FILE_SERVICE, enable);
        redisTemplate.opsForValue().set(ENABLE_ASYNC_FILE_SERVICE, enable);
    }

    @PostConstruct
    public void init() {
        Boolean value = redisTemplate.opsForValue().get(ENABLE_ASYNC_FILE_SERVICE);
        localCache.put(ENABLE_ASYNC_FILE_SERVICE, Objects.requireNonNullElse(value, false));
    }
}
