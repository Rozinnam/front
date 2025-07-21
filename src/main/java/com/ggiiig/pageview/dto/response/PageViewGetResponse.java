package com.ggiiig.pageview.dto.response;

import com.ggiiig.pageview.entity.PageType;

import java.time.LocalDate;

public interface PageViewGetResponse {
    PkInfo getPk();
    Long getViewCount();

    interface PkInfo {
        LocalDate getViewDate();
        PageType getPageType();
    }
}
