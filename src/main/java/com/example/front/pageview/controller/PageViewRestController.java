package com.example.front.pageview.controller;

import com.example.front.pageview.dto.response.PageViewGetResponse;
import com.example.front.pageview.service.PageViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pageview")
@RequiredArgsConstructor
public class PageViewRestController {
    private final PageViewCountService pageViewCountService;

    @GetMapping()
    public List<PageViewGetResponse> getPageViewCount(@RequestParam LocalDate date) {
        return pageViewCountService.getPageViewByDate(date);
    }
}
