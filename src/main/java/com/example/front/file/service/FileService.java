package com.example.front.file.service;

import com.example.front.file.adaptor.FileAdaptor;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.file.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileAdaptor fileAdaptor;
    private final FileUtils fileUtils;

    public String fileUpload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FileEmptyException();
        }

        for (MultipartFile file : files) {
            if (!fileUtils.isValidMimeType(file)) {
                throw new FileUnsupportedFormatException();
            }
        }

        return fileAdaptor.fileUpload(files);
    }
}
