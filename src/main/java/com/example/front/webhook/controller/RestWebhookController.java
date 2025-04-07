package com.example.front.webhook.controller;

import com.example.front.file.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ConditionalOnProperty(name = "app.mode", havingValue = "async")
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class RestWebhookController {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/result")
    public ResponseEntity<String> receiveResult(@RequestBody ResponseDto result) {
        String taskId = result.getTaskId();

        if (taskId == null || taskId.isBlank()) {
            log.error("TaskId is null or empty");
            return ResponseEntity.badRequest().body("TaskId가 유효하지 않습니다.");
        }

        messagingTemplate.convertAndSend("/topic/result/" + taskId, result);
        log.info("TaskId : {}", taskId);

        return ResponseEntity.ok("결과 전송 완료");
    }
}