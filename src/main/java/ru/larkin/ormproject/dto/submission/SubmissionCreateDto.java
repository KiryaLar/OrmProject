package ru.larkin.ormproject.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCreateDto {

    @NotNull
    private Integer studentId;
    @NotBlank
    @Size(max = 1000)
    private String content;
    @NotNull
    private Long assignmentId;
}
