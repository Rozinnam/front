package com.ggiiig.file.service;

import com.ggiiig.file.adaptor.SyncFileAdaptor;
import com.ggiiig.file.exception.CarPartEmptyException;
import com.ggiiig.file.exception.FileEmptyException;
import com.ggiiig.file.exception.FileUnsupportedFormatException;
import com.ggiiig.part.domain.CarPart;
import com.ggiiig.util.CarRepairCostCalculator;
import com.ggiiig.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncFileProcessor {
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

        return CarRepairCostCalculator.calculateForSync(syncFileAdaptor.communicateWithAiServer(files), carPart);
    }

}
