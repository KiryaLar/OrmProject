package ru.larkin.ormproject.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record LessonCreateDto(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 1000) String description,
        @NotNull @Positive Integer lessonOrder,
        @URL String videoUrl
) {}

