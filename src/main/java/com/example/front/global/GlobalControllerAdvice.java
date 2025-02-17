package com.example.front.global;

import com.example.front.file.UnExpectedStateException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientException(HttpClientErrorException e, Model model) {
        model.addAttribute("errorMessage", e.getResponseBodyAsString());

        return "/user/home";
    }

    @ExceptionHandler(UnExpectedStateException.class)
    public String handleUnExpectedStateException(Model model) {
        model.addAttribute("errorMessage", "예기치 못한 에러가 발생했습니다.\n 관리자에게 문의해주세요./");

        return "/user/home";
    }
}
