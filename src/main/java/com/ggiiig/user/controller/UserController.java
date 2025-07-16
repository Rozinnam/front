package com.ggiiig.user.controller;

import com.ggiiig.pageview.entity.PageType;
import com.ggiiig.pageview.service.PageViewCountService;
import com.ggiiig.user.entity.User;
import com.ggiiig.user.entity.UserRole;
import com.ggiiig.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PageViewCountService pageViewCountService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/admin";
    }

    @GetMapping("")
    public String getAdminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null || UserRole.ADMIN != user.getRole()) {
            return "redirect:/admin/login";
        }

        Map<PageType, Long> viewCounts = pageViewCountService.getAllViewCounts();
        model.addAttribute("viewCounts", viewCounts);

        return "admin/home";
    }

    @PostMapping("/login")
    public String requestLogin(@RequestParam String id, @RequestParam String password, HttpSession session, HttpServletRequest request) {
        session.invalidate();
        session = request.getSession(true);

        User user = userService.login(id, password);
        if (user == null) {
            return "redirect:/admin/login";
        }

        session.setAttribute("loginUser", user);
        session.setMaxInactiveInterval(600);

        return "redirect:/admin";
    }
}