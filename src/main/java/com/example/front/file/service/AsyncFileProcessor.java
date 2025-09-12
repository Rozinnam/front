package com.example.front.file.service;

import com.example.front.file.adaptor.AsyncFileAdaptor;
import com.example.front.file.exception.CarPartEmptyException;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.util.FileUtils;
import com.example.front.part.domain.CarPart;
import com.example.front.util.TaskCarPartRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncFileProcessor {
    private final AsyncFileAdaptor asyncFileAdaptor;
    private final FileUtils fileUtils;
    private final TaskCarPartRegistry taskCarPartRegistry;

    public String communicateWithAiServer(List<MultipartFile> files, CarPart carPart) {
        if (files == null || files.isEmpty()) {
            throw new FileEmptyException();
        }

        if (carPart == null) {
            throw new CarPartEmptyException();
        }

        for (MultipartFile file : files) {
            if (!fileUtils.isValidMimeType(file)) {
                throw new FileUnsupportedFormatException();
            }

            log.info("fileName : {}\n", file.getOriginalFilename());
        }

        String taskId = generateTaskId();
        taskCarPartRegistry.register(taskId, carPart);
        asyncFileAdaptor.communicateWithAiServer(files, taskId);
        log.info("taskId : {}\n", taskId);

        return taskId;
    }

    private String generateTaskId() {
        return UUID.randomUUID().toString();
    }
}
