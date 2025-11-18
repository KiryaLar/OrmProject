package ru.larkin.ormproject.dto.module;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.larkin.ormproject.dto.lesson.LessonCreateDto;

import java.util.List;

public record ModuleCreateDto(
        @NotBlank @Size(max = 255) String title,
        @NotNull @Positive Integer moduleOrder,
        @Valid List<LessonCreateDto> lessons
) {}
