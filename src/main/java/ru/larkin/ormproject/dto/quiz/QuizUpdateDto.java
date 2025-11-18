package ru.larkin.ormproject.dto.quiz;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.larkin.ormproject.entity.QuizType;

import java.time.Instant;

public record QuizUpdateDto(
        QuizType quizType,
        @Size(max = 255) String title,
        @Positive Integer maxScore,
        @FutureOrPresent Instant startDate,
        @Positive Integer durationInMinutes,
        Integer courseId,
        Long moduleId
) {}

