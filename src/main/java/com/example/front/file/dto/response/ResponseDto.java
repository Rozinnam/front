package com.example.front.file.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {
    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("Breakage_3")
    private float breakage;

    @JsonProperty("Crushed_2")
    private float crushed;

    @JsonProperty("Scratch_0")
    private float scratch;

    @JsonProperty("Seperated_1")
    private float seperated;
}
