package com.example.front.pageview.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PageTypeConverter implements AttributeConverter<PageType, String> {
    @Override
    public String convertToDatabaseColumn(PageType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PageType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : PageType.fromValue(dbData);
    }
}
