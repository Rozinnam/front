package com.example.front.file.utils;

import com.example.front.config.FileProperties;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.UnExpectedStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtils {
    private final FileProperties fileProperties;
    private final Tika tika;

    public boolean isValidMimeType(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException();
        }

        String mimeType;

        try {
            mimeType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new UnExpectedStateException(e);
        }

        return fileProperties.getSupportedTypes().contains(mimeType);
    }
}
