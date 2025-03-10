package com.example.front.file.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DamageClassDto {
    @JsonProperty("damage_class")
    private int damageClass;
}
