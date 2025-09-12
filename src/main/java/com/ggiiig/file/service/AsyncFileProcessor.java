package com.ggiiig.file.service;

import com.ggiiig.file.adaptor.AsyncFileAdaptor;
import com.ggiiig.file.exception.CarPartEmptyException;
import com.ggiiig.file.exception.FileEmptyException;
import com.ggiiig.file.exception.FileUnsupportedFormatException;
import com.ggiiig.util.FileUtils;
import com.ggiiig.part.domain.CarPart;
import com.ggiiig.util.TaskCarPartRegistry;
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
