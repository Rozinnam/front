package com.example.front.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
@Validated
public class FileProperties {
    @NotEmpty(message = "지원되는 파일 형식을 지정해야 합니다.")
    /* 지원되는 파일 MIME 타입 목록 */
    private Set<String> supportedTypes;

    @NotEmpty(message = "최대 파일 크기를 지정해야 합니다.")
    /* 최대 파일 크기 (예: "10MB") */
    private String maxSize;

    @Min(value = 1, message = "최대 파일 개수는 1 이상이어야 합니다.")
    /* 업로드 가능한 최대 파일 개수 */
    private int maxCount;
}