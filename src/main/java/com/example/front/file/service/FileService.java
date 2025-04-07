package com.example.front.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String communicateWithAiServer(List<MultipartFile> files);
}
