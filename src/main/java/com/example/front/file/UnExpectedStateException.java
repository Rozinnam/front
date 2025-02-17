package com.example.front.file;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnExpectedStateException extends RuntimeException {
    public UnExpectedStateException(Exception e) {
        log.info(e.getMessage());
    }
}
