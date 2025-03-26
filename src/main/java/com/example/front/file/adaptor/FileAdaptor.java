package com.example.front.file.adaptor;

import com.example.front.annotation.CalculateTime;
import com.example.front.config.BackAdaptorProperties;
import com.example.front.file.dto.response.ResponseDto;
import com.example.front.file.exception.AiServerCommunicationException;
import com.example.front.file.exception.UnExpectedStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileAdaptor {
    private final RestTemplate restTemplate;
    private final BackAdaptorProperties backAdaptorProperties;
    private static final String SCHEME = "http://";
    private static final String URL = "/predict";

    @CalculateTime
    public ResponseDto communicateWithAiServer(List<MultipartFile> files) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            for (MultipartFile file : files) {
                body.add("image", new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                });
            }
        } catch (Exception e) {
            throw new UnExpectedStateException(e);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        URI uri = UriComponentsBuilder.fromUriString(SCHEME + backAdaptorProperties.getAddress() + URL).build().toUri();

        ResponseEntity<ResponseDto> exchange = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                ResponseDto.class
        );

        if (exchange.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new AiServerCommunicationException("AI 서버와의 통신 실패: 응답 코드" + exchange.getStatusCode() + ", 본문: " + exchange.getBody(), null);
        }

        return exchange.getBody();
    }
}