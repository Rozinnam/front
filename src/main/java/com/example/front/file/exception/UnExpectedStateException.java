package com.example.front.file.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnExpectedStateException extends RuntimeException {
    public UnExpectedStateException(Exception e) {
        super(e);
        log.error("예상치 못한 상태 발생", e);
    }
}
