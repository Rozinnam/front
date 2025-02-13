package com.example.front.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class HomeController {
    public String home() {
        return "home";
    }
}
