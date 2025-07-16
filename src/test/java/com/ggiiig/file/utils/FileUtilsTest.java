package com.ggiiig.file.utils;

import com.ggiiig.config.FileProperties;
import com.ggiiig.file.exception.FileEmptyException;
import com.ggiiig.util.FileUtils;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileUtilsTest {
    @Mock
    private FileProperties fileProperties;
    @InjectMocks
    private FileUtils fileUtils;

    @Test
    void validMimeType_returnTrue_whenMimeTypeIsSupported() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", new byte[10]);
        Tika tika = mock(Tika.class);
        ReflectionTestUtils.setField(fileUtils, "tika", tika);

        when(tika.detect(any(InputStream.class))).thenReturn("image/png");
        when(fileProperties.getSupportedTypes()).thenReturn(Set.of("image/png"));

        assertTrue(fileUtils.isValidMimeType(mockFile));
    }

    @Test
    void throwFileEmptyException_whenFileIsEmpty() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        assertThrows(FileEmptyException.class, () -> fileUtils.isValidMimeType(mockFile));
    }
}