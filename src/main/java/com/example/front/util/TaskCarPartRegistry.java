package com.example.front.util;

import com.example.front.part.domain.CarPart;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskCarPartRegistry {
    private static class Entry {
        private final CarPart carPart;
        private final long creationTime;

        Entry(CarPart carPart) {
            this.carPart = carPart;
            this.creationTime = System.currentTimeMillis();
        }
    }

    private final Map<String, Entry> taskCarPartMap = new ConcurrentHashMap<>(16, 0.75f);

    public void register(String taskId, CarPart carPart) {
        taskCarPartMap.put(taskId, new Entry(carPart));
    }

    public CarPart getCarPart(String taskId) {
        return taskCarPartMap.get(taskId).carPart;
    }

    public void remove(String taskId) {
        taskCarPartMap.remove(taskId);
    }

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void cleanupExpiredEntries() {
        long now = System.currentTimeMillis();
        long expirationTime = 24L * 3600 * 1000; // 24시간

        taskCarPartMap.entrySet().removeIf(entry ->
                (now - entry.getValue().creationTime) > expirationTime);
    }
}