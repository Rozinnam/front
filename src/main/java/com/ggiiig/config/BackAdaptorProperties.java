package com.ggiiig.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.example.front.config.back")
@RequiredArgsConstructor
public class BackAdaptorProperties {
    private String address;
    private String webhookUrl;
}
