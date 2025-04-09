package com.example.front.file.dto.response;

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
        StringBuilder stringBuilder = new StringBuilder();

        if (this.taskId != null) {
            stringBuilder.append("taskId : ").append(this.taskId).append("\n");
        }
        stringBuilder.append("파손 : ").append(this.breakage).append("\n")
                .append("찌그러짐 : ").append(this.crushed).append("\n")
                .append("긁힘 : ").append(this.scratch).append("\n")
                .append("이격 : ").append(this.seperated);

        return stringBuilder.toString();
    }
}
