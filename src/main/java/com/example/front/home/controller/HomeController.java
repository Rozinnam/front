package com.example.front.home.controller;

import com.example.front.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final FileService fileService;

    @GetMapping("/")
    public String home() {
        return "/user/home";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") List<MultipartFile> files, Model model) {
        model.addAttribute("errorMessage", fileService.fileUpload(files));

        return "/user/home";
    }
}
