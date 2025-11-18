package ru.larkin.ormproject.dto.questionsubmission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.dto.selectedansweroption.SelectedAnswerOptionCreateDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSubmissionCreateDto {
    @NotNull
    private Long questionId;
    private List<SelectedAnswerOptionCreateDto> selectedOptions;
}

