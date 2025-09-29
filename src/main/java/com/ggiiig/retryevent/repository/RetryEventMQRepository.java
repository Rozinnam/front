package com.ggiiig.retryevent.repository;

import com.ggiiig.retryevent.entity.RetryEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RetryEventMQRepository {
    private final Queue<RetryEvent> memoryQueue = new ConcurrentLinkedQueue<>();

    public void offer(RetryEvent retryEvent) {
        memoryQueue.add(retryEvent);
    }

    public void offer(List<RetryEvent> retryEvents) {
        memoryQueue.addAll(retryEvents);
    }

    public RetryEvent poll() {
        return memoryQueue.poll();
    }
}
