package com.example.front.featureflag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeatureFlagListener {
    private final FeatureFlagService featureFlagService;

    public FeatureFlagListener(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public void onMessage(String message, String channel) {
        boolean enabled = Boolean.parseBoolean(message);
        featureFlagService.updateFlag(enabled);

        log.info("FeatureFlagListener : Feature flag updated: asyncEnabled = {}", enabled);
    }
}