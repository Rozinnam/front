package com.ggiiig.webhook.service;

import com.ggiiig.file.dto.response.ResponseDto;
import com.ggiiig.sse.service.SseService;
import com.ggiiig.util.CarRepairCostCalculator;
import com.ggiiig.util.TaskCarPartRegistry;
import com.ggiiig.webhook.dto.response.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebHookService {
    private final SseService sseService;
    private final TaskCarPartRegistry taskCarPartRegistry;

    public ResponseEntity<String> receiveResult(ResponseDto responseDto) {
        String taskId = responseDto.getTaskId();

        if (taskId == null || taskId.isBlank()) {
            log.error("TaskId is null or empty");
            return ResponseEntity.badRequest().body("TaskId가 유효하지 않습니다.");
        }

        ResultDto result = CarRepairCostCalculator.calculateForAsync(responseDto, taskCarPartRegistry.getCarPart(taskId));

        sseService.sendResult(taskId, result);
        log.info("결과 전송 완료: \n{}", taskId + result);
        taskCarPartRegistry.remove(taskId);

        return ResponseEntity.ok("결과 전송 완료");
    }
}
