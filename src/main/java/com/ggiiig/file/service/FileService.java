package com.ggiiig.file.service;

import com.ggiiig.part.domain.CarPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String estimateRepairCost(List<MultipartFile> files, CarPart carPart);
}
