package ru.larkin.ormproject.dto.module;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ModuleUpdateDto(
        @Size(max = 255) String title,
        @Positive Integer moduleOrder,
        Integer courseId
) {}

