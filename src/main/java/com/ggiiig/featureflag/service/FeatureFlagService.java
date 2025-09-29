package com.ggiiig.featureflag.service;

import com.ggiiig.featureflag.repository.FeatureFlagRedisRepository;
import com.ggiiig.retryevent.entity.RetryEvent;
import com.ggiiig.retryevent.service.RetryEventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagService {
    private final FeatureFlagRedisRepository featureFlagRedisRepository;
    private final RetryEventService retryEventService;

    private final Map<String, Boolean> localCache = new ConcurrentHashMap<>();
    private static final String ENABLE_ASYNC_FILE_SERVICE = "enable_async_file_service";
    private static final boolean DEFAULT_ASYNC_FILE_SERVICE_MODE = true;

    public boolean isEnableAsyncFileServiceMode() {
        return localCache.getOrDefault(ENABLE_ASYNC_FILE_SERVICE, DEFAULT_ASYNC_FILE_SERVICE_MODE);
    }

    public void updateFlag(boolean enable) {
        localCache.put(ENABLE_ASYNC_FILE_SERVICE, enable);

        if (!featureFlagRedisRepository.safeSet(ENABLE_ASYNC_FILE_SERVICE, enable)) {
            retryEventService.enqueue(new RetryEvent(ENABLE_ASYNC_FILE_SERVICE, enable));
        }
    }

    @Scheduled(cron = "0 0 */2 * * *")
    public void syncFeatureFlagsFromRedis() {
        loadFeatureFlagsFromRedis();
    }

    @PostConstruct
    public void init() {
        loadFeatureFlagsFromRedis();
    }

    private void loadFeatureFlagsFromRedis() {
        Boolean value = featureFlagRedisRepository.safeGet(ENABLE_ASYNC_FILE_SERVICE)
                .orElse(DEFAULT_ASYNC_FILE_SERVICE_MODE);
        localCache.put(ENABLE_ASYNC_FILE_SERVICE, value);
    }
}
