package com.ggiiig.file.exception;

public class AiServerCommunicationException extends RuntimeException {

    public AiServerCommunicationException(String message, Exception e) {
        super(message, e);
    }
}
