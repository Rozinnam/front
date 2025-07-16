package com.ggiiig.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ConditionalOnProperty(name = "app.mode", havingValue = "async")
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseRestController {

    // 연결 관리용 맵
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/sse/result/{taskId}")
    public SseEmitter subscribe(@PathVariable String taskId) {
        SseEmitter emitter = new SseEmitter(60 * 10000L);

        emitters.put(taskId, emitter);

        emitter.onCompletion(() -> emitters.remove(taskId));
        emitter.onTimeout(() -> emitters.remove(taskId));
        emitter.onError(e -> emitters.remove(taskId));

        return emitter;
    }

    public void sendResult(String taskId, String result) {
        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(result));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } finally {
                emitters.remove(taskId);
            }
        }
    }
}
