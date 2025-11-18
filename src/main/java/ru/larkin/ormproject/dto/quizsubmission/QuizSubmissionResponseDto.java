package ru.larkin.ormproject.dto.quizsubmission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionResponseDto {
    private Long id;
    private Long quizId;
    private Integer userId;
    private Integer totalScore;
    private Instant submitTime;
}
