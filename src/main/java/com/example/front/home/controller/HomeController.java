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

    @PostMapping("/upload")
    public String upload(@RequestParam("files") List<MultipartFile> files, Model model) {
        model.addAttribute("result", fileService.communicateWithAiServer(files));

        return isSyncMode() ? "user/result_sync" : "user/result_async"; 
    }

    private boolean isSyncMode() {
        return appMode.equalsIgnoreCase("async");
    }
}
