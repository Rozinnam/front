package com.ggiiig.file.adaptor;

import com.ggiiig.config.BackAdaptorProperties;
import com.ggiiig.file.dto.response.ResponseDto;
import com.ggiiig.file.exception.AiServerCommunicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncFileAdaptorTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BackAdaptorProperties backAdaptorProperties;

    @InjectMocks
    private SyncFileAdaptor syncFileAdaptor;

    @Test
    void communicateWithAiServer_returnResponseDto_whenRequestIsSuccessful() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "test.png", "image/png", "img-data".getBytes());
        List<MultipartFile> files = List.of(mockFile);

        URI uri = URI.create("http://localhost/predict");
        when(backAdaptorProperties.getAddress()).thenReturn("localhost");

        ResponseDto fakeResponse = new ResponseDto();
        ResponseEntity<ResponseDto> responseEntity = new ResponseEntity<>(fakeResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(uri),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ResponseDto.class)
        )).thenReturn(responseEntity);

        ResponseDto result = syncFileAdaptor.communicateWithAiServer(files);

        assertEquals(fakeResponse, result);
    }

    @Test
    void communicateWithServer_throwAiServerCommunicationException_whenAiServerDoesntReturnHttpStatusCode200() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "test.png", "image/png", "img-data".getBytes());
        List<MultipartFile> files = List.of(mockFile);

        when(backAdaptorProperties.getAddress()).thenReturn("localhost");

        ResponseEntity<ResponseDto> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(
                any(),
                any(),
                any(),
                eq(ResponseDto.class)
        )).thenReturn(responseEntity);

        assertThrows(AiServerCommunicationException.class, () -> syncFileAdaptor.communicateWithAiServer(files));
    }
}