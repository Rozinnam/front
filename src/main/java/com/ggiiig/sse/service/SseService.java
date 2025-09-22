package com.ggiiig.sse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggiiig.webhook.dto.response.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final ObjectMapper objectMapper;
    // 연결 관리용 맵
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String taskId) {
        SseEmitter emitter = new SseEmitter(60 * 10000L);

        emitters.put(taskId, emitter);

        emitter.onCompletion(() -> emitters.remove(taskId));
        emitter.onTimeout(() -> {
            emitters.remove(taskId);
            log.error("{} timed out", taskId);
        });
        emitter.onError(e -> emitters.remove(taskId));

        return emitter;
    }

    public void sendResult(String taskId, ResultDto result) {
        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(objectMapper.writeValueAsString(result)));
                emitter.complete();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                emitters.remove(taskId);
            }
        }
    }
}
