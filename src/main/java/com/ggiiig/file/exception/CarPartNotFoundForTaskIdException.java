package com.ggiiig.file.exception;

public class CarPartNotFoundForTaskIdException extends RuntimeException {
    public CarPartNotFoundForTaskIdException(String taskId) {
        super("TaskId에 해당하는 CarPart를 찾을 수 없습니다. taskId=" + taskId);
    }
}
