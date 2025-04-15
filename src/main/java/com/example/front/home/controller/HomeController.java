package com.example.front.home.controller;

import com.example.front.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    @Value("${app.mode}")
    private String appMode;

    private final FileService fileService;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @GetMapping("/request")
    public String getRequestPage() {
        return "user/request";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") List<MultipartFile> files, Model model) {
        String result = fileService.communicateWithAiServer(files);

        if (isSyncMode()) {
            model.addAttribute("result", result);
            return "user/result_sync";
        } else if (isAsyncMode()) {
            model.addAttribute("taskId", result);
            return "user/result_async";
        }

        throw new IllegalStateException("앱 모드 설정을 확인해주세요.");
    }

    private boolean isSyncMode() {
        return appMode.equalsIgnoreCase("sync");
    }
    private boolean isAsyncMode() {
        return appMode.equalsIgnoreCase("async");
    }
}
