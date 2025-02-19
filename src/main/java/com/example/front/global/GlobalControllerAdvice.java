package com.example.front.global;

import com.example.front.config.FileProperties;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.file.exception.UnExpectedStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final FileProperties fileProperties;

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientException(HttpClientErrorException e, Model model) {
        model.addAttribute("errorMessage", e.getResponseBodyAsString());

        return "/user/home";
    }

    @ExceptionHandler({UnExpectedStateException.class, RuntimeException.class})
    public String handleUnExpectedStateException(Model model) {
        model.addAttribute("errorMessage", "예기치 못한 에러가 발생했습니다.\n 관리자에게 문의해주세요./");

        return "/user/home";
    }

    @ExceptionHandler(FileUnsupportedFormatException.class)
    public String fileUnsupportedFormatException(Model model) {
        model.addAttribute("errorMessage", "지원되지 않는 파일 형식입니다.\n지원 파일 형식: "
                + String.join(", ", fileProperties.getSupportedTypes()));

        return "/user/home";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxFilesExceededException(Model model) {
        model.addAttribute("errorMessage",
                "각 파일은 최대 " + fileProperties.getMaxSize() + "까지 업로드 가능합니다.");

        return "/user/home";
    }

    @ExceptionHandler(MultipartException.class)
    public String fileSizeTooLargeException(MultipartException e, Model model) {
        if (e.getCause() instanceof IllegalStateException) {
            model.addAttribute("errorMessage",
                    "최대 " + fileProperties.getMaxCount() + "개의 파일만 업로드할 수 있습니다.");
        } else {
            model.addAttribute("errorMessage", "파일 업로드 중 알 수 없는 오류가 발생했습니다.");
        }

        return "/user/home";
    }
}
