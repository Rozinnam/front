package com.ggiiig.webhook.controller;

import com.ggiiig.file.dto.response.ResponseDto;
import com.ggiiig.webhook.service.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final WebHookService webHookService;

    @PostMapping("/result")
    public ResponseEntity<String> receiveResult(@RequestBody ResponseDto responseDto) {
        return webHookService.receiveResult(responseDto);
    }
}