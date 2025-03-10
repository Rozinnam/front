package com.example.front.file.adaptor;

import com.example.front.config.BackAdaptorProperties;
import com.example.front.file.exception.UnExpectedStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class FileAdaptor {
    private final RestTemplate restTemplate;
    private final BackAdaptorProperties backAdaptorProperties;
    private static final String URL = "/predict";

    public String fileUpload(List<MultipartFile> files) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            for (MultipartFile file : files) {
                log.info("\n\nAdaptor, FileName : " + file.getOriginalFilename() + "\n\n");

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
        URI uri = UriComponentsBuilder.fromUriString(backAdaptorProperties.getAddress() + URL).build().toUri();

        log.info("\n\nURI : " + uri + "\n\n");

        ResponseEntity<String> exchange = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (exchange.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException();
        }

        return exchange.getBody();
    }
}