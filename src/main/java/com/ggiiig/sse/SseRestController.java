package com.ggiiig.sse;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@lombok.extern.slf4j.Slf4j
@Slf4j
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseRestController {
    private final SseService sseService;

    @GetMapping("/result/{taskId}")
    public SseEmitter subscribe(@PathVariable String taskId) {
        return sseService.subscribe(taskId);
    }
}
