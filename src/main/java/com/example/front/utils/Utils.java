package com.example.front.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

public class Utils {
    private Utils() {}

    public static HttpHeaders getHttpHeader() {
        return getHttpHeader(MediaType.MULTIPART_FORM_DATA, List.of(MediaType.APPLICATION_JSON));
    }

    public static HttpHeaders getHttpHeader(MediaType mediaType, List<MediaType> mediaTypeList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setAccept(mediaTypeList);

        return headers;
    }
}
