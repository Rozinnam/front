package com.example.front.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private Set<String> supportedTypes;
    private String maxSize;
    private int maxCount;
}