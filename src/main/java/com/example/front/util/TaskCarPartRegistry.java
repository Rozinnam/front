package com.example.front.util;

import com.example.front.part.domain.CarPart;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskCarPartRegistry {
    private final Map<String, CarPart> taskCarPartMap = new ConcurrentHashMap<>();

    public void register(String taskId, CarPart carPart) {
        taskCarPartMap.put(taskId, carPart);
    }

    public CarPart getCarPart(String taskId) {
        return taskCarPartMap.get(taskId);
    }

    public void remove(String taskId) {
        taskCarPartMap.remove(taskId);
    }
}