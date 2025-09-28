package com.ggiiig.retryevent.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class RetryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer retryEventId;

    @Column(name = "key")
    @NotNull
    private String key;

    @Column(name = "value")
    @NotNull
    private Boolean value;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    @Nullable
    private LocalDateTime processedAt;

    public RetryEvent(String key, Boolean value) {
        this.key = key;
        this.value = value;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void process() {
        this.status = Status.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }
}
