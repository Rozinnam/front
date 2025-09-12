package com.ggiiig.webhook.controller;

import com.ggiiig.file.dto.response.ResponseDto;
import com.ggiiig.util.CarRepairCostCalculator;
import com.ggiiig.sse.SseRestController;
import com.ggiiig.util.TaskCarPartRegistry;
import com.ggiiig.webhook.dto.response.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class RestWebhookController {
    private final SseRestController sseRestController;
    private final TaskCarPartRegistry taskCarPartRegistry;

    @PostMapping("/result")
    public ResponseEntity<String> receiveResult(@RequestBody ResponseDto responseDto) {
        String taskId = responseDto.getTaskId();

        if (taskId == null || taskId.isBlank()) {
            log.error("TaskId is null or empty");
            return ResponseEntity.badRequest().body("TaskId가 유효하지 않습니다.");
        }

        ResultDto result = CarRepairCostCalculator.calculateForAsync(responseDto, taskCarPartRegistry.getCarPart(taskId));

        sseRestController.sendResult(taskId, result);
        log.info("결과 전송 완료: \n{}", taskId + result);
        taskCarPartRegistry.remove(taskId);

        return ResponseEntity.ok("결과 전송 완료");
    }
}