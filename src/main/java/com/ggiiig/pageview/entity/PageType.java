package com.ggiiig.pageview.entity;

import lombok.Getter;

@Getter
public enum PageType {
    HOME("home"),
    REQUEST("request"),
    RESULT("result");

    private final String value;

    PageType(String value) {
        this.value = value;
    }

    public static PageType fromValue(String value) {
        for (PageType pageType : PageType.values()) {
            if (pageType.getValue().equals(value)) {
                return pageType;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}