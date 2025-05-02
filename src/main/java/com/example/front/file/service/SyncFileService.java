package com.example.front.file.service;

import com.example.front.file.adaptor.SyncFileAdaptor;
import com.example.front.file.exception.CarPartEmptyException;
import com.example.front.file.exception.FileEmptyException;
import com.example.front.file.exception.FileUnsupportedFormatException;
import com.example.front.part.domain.CarPart;
import com.example.front.util.CarRepairCostCalculator;
import com.example.front.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.mode", havingValue = "sync", matchIfMissing = true)
@RequiredArgsConstructor
public class SyncFileService implements FileService {
    private final SyncFileAdaptor syncFileAdaptor;
    private final FileUtils fileUtils;

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

        return CarRepairCostCalculator.calculate(syncFileAdaptor.communicateWithAiServer(files), carPart);
    }

}
