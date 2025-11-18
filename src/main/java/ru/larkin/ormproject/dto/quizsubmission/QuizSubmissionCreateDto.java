package ru.larkin.ormproject.dto.quizsubmission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.dto.questionsubmission.QuestionSubmissionCreateDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmissionCreateDto {
    @NotNull Long quizId;
    @NotNull Integer userId;
    List<QuestionSubmissionCreateDto> questionSubmissions;
}

