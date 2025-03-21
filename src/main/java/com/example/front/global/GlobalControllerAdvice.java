package com.example.front.global;

import com.example.front.config.FileProperties;
import com.example.front.file.exception.AiServerCommunicationException;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.file.exception.UnExpectedStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final FileProperties fileProperties;

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

    @ExceptionHandler(FileEmptyException.class)
    public String handlerFileEmptyException(FileEmptyException e, Model model) {
        model.addAttribute("errorMessage", "업로드할 파일을 선택해 주세요.\n" +
                "파일 선택 버튼을 클릭하여 파일을 추가하실 수 있습니다.");

        return "/user/home";
    }

    @ExceptionHandler(UnExpectedStateException.class)
    public String handleUnExpectedStateException(Exception e, Model model) {
        log.error("예기치 못한 에러 발생", e);
        model.addAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "/user/home";
    }

    @ExceptionHandler(AiServerCommunicationException.class)
    public String handleAiServerCommunicationException(Exception e, Model model) {
        log.error("AI 서버와 통신 중 오류 발생 : {}", e.getMessage(), e);
        model.addAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "/user/home";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("처리 되지 않은 예외 발생", e);
        model.addAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "/user/home";
    }
}
