package com.example.front.file.service;

import com.example.front.file.adaptor.FileAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileAdaptor fileAdaptor;

    public boolean fileUpload(MultipartFile file) {
        fileAdaptor.fileUpload(file);

        return true;
    }
}
