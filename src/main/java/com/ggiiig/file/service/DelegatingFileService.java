package com.ggiiig.file.service;

import com.ggiiig.featureflag.FeatureFlagService;
import com.ggiiig.part.domain.CarPart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DelegatingFileService implements FileService {
    private final SyncFileProcessor syncFileProcessor;
    private final AsyncFileProcessor asyncFileProcessor;
    private final FeatureFlagService featureFlagService;

    @Override
    public String estimateRepairCost(List<MultipartFile> files, CarPart carPart) {
        if (featureFlagService.isEnableAsyncFileServiceMode()) {
            return asyncFileProcessor.prepareRequest(files, carPart);
        }

        return syncFileProcessor.prepareRequest(files, carPart);
    }
}
