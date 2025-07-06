package com.example.front.home.controller;

import com.example.front.annotation.CalculateTime;
import com.example.front.file.service.FileService;
import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.service.PageViewCountService;
import com.example.front.part.domain.CarPart;
import jakarta.servlet.http.HttpServletRequest;
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
    private final PageViewCountService pageViewCountService;

//    @GetMapping("/")
//    public String home(HttpServletRequest request) {
//        pageViewCountService.handleViewCount(getClientIP(request), PageType.HOME);
//
//        return "user/home";
//    }

    @GetMapping("/")
    public String getHomePage(HttpServletRequest request, Model model) {
        model.addAttribute("carParts", CarPart.values());
        pageViewCountService.handleViewCount(getClientIP(request), PageType.REQUEST);

        return "user/request";
    }

    @CalculateTime
    @PostMapping("/upload")
    public String upload(@RequestParam("files") List<MultipartFile> files,
                         @RequestParam("selectedCarPart") CarPart carPart,
                         HttpServletRequest request,
                         Model model) {
        String result = fileService.communicateWithAiServer(files, carPart);
        pageViewCountService.handleViewCount(getClientIP(request), PageType.RESULT);

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

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null) {
            return xfHeader.split(", ")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
