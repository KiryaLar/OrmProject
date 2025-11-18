package ru.larkin.ormproject.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponseDto {
    private Long id;
    private Integer studentId;
    private String content;
    private Integer score;
    private String feedback;
    private Instant submittedAt;
    private Long assignmentId;
}
