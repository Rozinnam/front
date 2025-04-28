package com.example.front.file.service;

import com.example.front.part.domain.CarPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String communicateWithAiServer(List<MultipartFile> files, CarPart carPart);
}
