package com.ggiiig.center.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCenterDto {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Double rating;
    private String description;
    private String operatingHours;
}
