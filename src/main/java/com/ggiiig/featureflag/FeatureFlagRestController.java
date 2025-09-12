package com.ggiiig.featureflag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/feature")
@Slf4j
public class FeatureFlagRestController {
    private final RedisTemplate<String, Boolean> redisTemplate;
    private static final String CHANNEL = "feature_flag:async_file_service";

    @PostMapping("/{enabled}")
    public String setFlag(@PathVariable boolean enabled) {
        redisTemplate.convertAndSend(CHANNEL, Boolean.toString(enabled));
        log.info("FeatureFlagRestController : Published = {}", enabled);

        return "Published " + enabled;
    }
}
