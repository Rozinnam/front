package com.ggiiig.retryevent.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "retry_event")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RetryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retry_event_id")
    private Integer retryEventId;

    @Column(name = "retry_event_key")
    @NotNull
    private String key;

    @Column(name = "retry_event_value")
    @NotNull
    private Boolean value;

    @Column(name = "retry_event_status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @Column(name = "retry_event_created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "retry_event_processed_at")
    @Nullable
    private LocalDateTime processedAt;

    public RetryEvent(String key, Boolean value) {
        this.key = key;
        this.value = value;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void startProcessing() {
        this.status = Status.PROCESSING;
    }

    public void complete() {
        this.status = Status.COMPLETED;
        this.processedAt = LocalDateTime.now();
    }

    public void resetToPending() {
        this.status = Status.PENDING;
    }
}
