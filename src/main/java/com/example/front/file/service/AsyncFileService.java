package com.example.front.file.service;

import com.example.front.file.adaptor.AsyncFileAdaptor;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.file.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.mode", havingValue = "async")
@RequiredArgsConstructor
public class AsyncFileService implements FileService {
    private final AsyncFileAdaptor asyncFileAdaptor;
    private final FileUtils fileUtils;

    @Override
    public String communicateWithAiServer(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FileEmptyException();
        }

        for (MultipartFile file : files) {
            if (!fileUtils.isValidMimeType(file)) {
                throw new FileUnsupportedFormatException();
            }
        }

        String taskId = generateTaskId();
        asyncFileAdaptor.communicateWithAiServer(files, taskId);

        return taskId;
    }

    private String generateTaskId() {
        return UUID.randomUUID().toString();
    }
}
