package com.ggiiig.file.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "파손 : " + this.breakage + "\n" +
                "찌그러짐 : " + this.crushed + "\n" +
                "긁힘 : " + this.scratch + "\n" +
                "이격 : " + this.seperated;
    }
}
