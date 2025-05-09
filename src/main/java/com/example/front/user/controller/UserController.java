package com.example.front.user.controller;

import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.service.PageViewCountService;
import com.example.front.user.entity.User;
import com.example.front.user.entity.UserRole;
import com.example.front.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PageViewCountService pageViewCountService;

    @GetMapping("/login")
    public String getLoginPage() {
        log.info("getLoginPage()");
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        log.info("logout()");

        return "redirect:/admin";
    }

    @GetMapping("")
    public String getAdminPage(HttpSession session, Model model) {
        log.info("getAdminPage()");
        User user = (User) session.getAttribute("loginUser");
        if (user == null || UserRole.ADMIN != user.getRole()) {
            log.info("redirect:/admin/login");
            return "redirect:/admin/login";
        }

        Map<PageType, Long> viewCounts = pageViewCountService.getAllViewCounts();
        model.addAttribute("viewCounts", viewCounts);

        return "admin/home";
    }

    @PostMapping("/login")
    public String requestLogin(@RequestParam String id, @RequestParam String password, HttpSession session, HttpServletRequest request) {
        log.info("requestLogin()");
        session.invalidate();
        session = request.getSession(true);

        User user = userService.login(id, password);
        if (user == null) {
            log.info("redirect:/admin/login");
            return "redirect:/admin/login";
        }

        session.setAttribute("loginUser", user);
        session.setMaxInactiveInterval(600);

        log.info("redirect:/admin");
        return "redirect:/admin";
    }
}