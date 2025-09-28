package com.ggiiig.retryevent.service;

import com.ggiiig.featureflag.repository.FeatureFlagRedisRepository;
import com.ggiiig.retryevent.entity.RetryEvent;
import com.ggiiig.retryevent.entity.Status;
import com.ggiiig.retryevent.repository.RetryEventMQRepository;
import com.ggiiig.retryevent.repository.RetryEventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryEventService {
    private final FeatureFlagRedisRepository featureFlagRedisRepository;
    private final RetryEventRepository retryEventRepository;
    private final RetryEventMQRepository  retryEventMQRepository;

    public void enqueue(RetryEvent retryEvent) {
        RetryEvent savedRetryEvent = retryEventRepository.save(retryEvent);
        retryEventMQRepository.offer(savedRetryEvent);
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void processQueue() {
        RetryEvent retryEvent;
        String key;
        Boolean value;

        while ((retryEvent = retryEventMQRepository.poll()) != null) {
            try {
                key =  retryEvent.getKey();
                value = retryEvent.getValue();
                featureFlagRedisRepository.safeSet(key, value);

                retryEvent.process();
                retryEventRepository.save(retryEvent);
            } catch (Exception e) {
                log.error("재시도 실패, {}", retryEvent);
                retryEventMQRepository.offer(retryEvent);
            }
        }
    }

    @PostConstruct
    public void getRetryEventFromDatabase() {
        List<RetryEvent> retryEvents = retryEventRepository.findByStatus(Status.PENDING);
        retryEvents.sort(Comparator.comparing(RetryEvent::getRetryEventId));
        retryEventMQRepository.offer(retryEvents);
    }
}
