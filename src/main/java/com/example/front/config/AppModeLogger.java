package com.example.front.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppModeLogger {
    @Value("${app.mode}")
    private String appMode;

    @PostConstruct
    public void logAppMode() {
        log.info("✅ 현재 앱 모드: {}", appMode);
    }
}