package ru.larkin.ormproject.dto.lesson;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record LessonUpdateDto(
        @Size(max = 255) String title,
        @Size(max = 1000) String description,
        @Positive Integer lessonOrder,
        @URL String videoUrl,
        Long moduleId
) {}

