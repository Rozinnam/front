package com.example.front.global;

import com.example.front.config.FileProperties;
import com.example.front.file.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final FileProperties fileProperties;

    @ExceptionHandler(FileUnsupportedFormatException.class)
    public String fileUnsupportedFormatException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "지원되지 않는 파일 형식입니다.\n지원 파일 형식: "
                + String.join(", ", fileProperties.getSupportedTypes()));

        return "redirect:/";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String maxFilesExceededException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "각 파일은 최대 " + fileProperties.getMaxSize() + "까지 업로드 가능합니다.");

        return "redirect:/";
    }

    @ExceptionHandler(MultipartException.class)
    public String fileSizeTooLargeException(MultipartException e, RedirectAttributes redirectAttributes) {
        if (e.getCause() instanceof IllegalStateException) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "최대 " + fileProperties.getMaxCount() + "개의 파일만 업로드할 수 있습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 알 수 없는 오류가 발생했습니다.");
        }

        return "redirect:/";
    }

    @ExceptionHandler(FileEmptyException.class)
    public String handlerFileEmptyException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "업로드할 파일을 선택해 주세요.\n" +
                "파일 선택 버튼을 클릭하여 파일을 추가하실 수 있습니다.");

        return "redirect:/";
    }

    @ExceptionHandler(CarPartEmptyException.class)
    public String handleCarPartEmptyException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "파손된 부품을 선택해 주세요.");

        return "redirect:/";
    }

    @ExceptionHandler(UnExpectedStateException.class)
    public String handleUnExpectedStateException(Exception e, RedirectAttributes redirectAttributes) {
        log.error("예기치 못한 에러 발생", e);
        redirectAttributes.addFlashAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "redirect:/";
    }

    @ExceptionHandler(CarPartNotFoundForTaskIdException.class)
    public String handleCarPartNotFoundForTaskIdException(CarPartNotFoundForTaskIdException e, RedirectAttributes redirectAttributes) {
        log.error("TaskId 에 해당하는 CarPart 찾지 못함 오류 발생", e);
        redirectAttributes.addFlashAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "redirect:/";
    }

    @ExceptionHandler(AiServerCommunicationException.class)
    public String handleAiServerCommunicationException(Exception e, RedirectAttributes redirectAttributes) {
        log.error("AI 서버와 통신 중 오류 발생 : {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(Exception e, RedirectAttributes redirectAttributes) {
        log.error("IllegalArgumentException 발생 : \n{}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return "redirect:/";
    }

    //RestTemplate 관련 예외
    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView handleHttpClientError(HttpClientErrorException e) {
        log.error("HttpClientErrorException 발생 : {}", e.getMessage(), e);
        ModelAndView mav = new ModelAndView("error/4xx");
        mav.addObject("status", e.getStatusCode().value());
        mav.addObject("message", e.getStatusText());

        return mav;
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ModelAndView handleHttpServerError(HttpServerErrorException e) {
        log.error("HttpServerErrorException 발생 : {}", e.getMessage(), e);
        ModelAndView mav = new ModelAndView("error/5xx");
        mav.addObject("status", e.getStatusCode().value());
        mav.addObject("message", e.getStatusText());

        return mav;
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleNetworkError(ResourceAccessException e) {
        log.error("ResourceAccessException 발생 : {}", e.getMessage(), e);
        ModelAndView mav = new ModelAndView("error/network");
        mav.addObject("message", "서버에 연결할 수 없습니다. 네트워크를 확인해주세요.");

        return mav;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleIllegalStateException(IllegalStateException e) {
        log.error("IllegalStateException 발생 : {}", e.getMessage(), e);
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("message", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return mav;
    }

    //GenericException
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        log.error("처리 되지 않은 예외 발생", e);
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("message", "시스템 오류가 발생했습니다.\n" +
                "잠시 후 다시 시도해주시거나 문제가 지속되면 관리자에게 문의해주세요.");

        return mav;
    }
}
