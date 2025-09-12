package com.ggiiig.file.exception;

public class CarPartNotFoundForTaskIdException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CarPartNotFoundForTaskIdException(String taskId) {
        super("TaskId에 해당하는 CarPart를 찾을 수 없습니다. taskId=" + taskId);
    }
}
