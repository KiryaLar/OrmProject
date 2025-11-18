package ru.larkin.ormproject.dto.assignment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record AssignmentCreateDto(
        @NotBlank @Size(max = 255) String title,
        @Size(max = 500) String description,
        @NotNull @FutureOrPresent Instant dueDate,
        @NotNull @Positive Integer maxScore,
        @NotNull Long lessonId
) {}

