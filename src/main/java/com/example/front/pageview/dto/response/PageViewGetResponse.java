package com.example.front.pageview.dto.response;

import com.example.front.pageview.entity.PageType;

import java.time.LocalDate;

public interface PageViewGetResponse {
    PkInfo getPk();
    Long getViewCount();

    interface PkInfo {
        LocalDate getViewDate();
        PageType getPageType();
    }
}
