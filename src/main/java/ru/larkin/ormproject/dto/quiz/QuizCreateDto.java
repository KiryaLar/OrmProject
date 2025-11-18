package ru.larkin.ormproject.dto.quiz;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.larkin.ormproject.entity.QuizType;

import java.time.Instant;

public record QuizCreateDto(
        @NotNull QuizType quizType,
        @NotBlank @Size(max = 255) String title,
        @NotNull @Positive Integer maxScore,
        @NotNull @FutureOrPresent Instant startDate,
        @NotNull @Positive Integer durationInMinutes,
        @NotNull Integer courseId,
        Long moduleId
) {}

