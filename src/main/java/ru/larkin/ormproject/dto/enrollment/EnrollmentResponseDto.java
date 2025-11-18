package ru.larkin.ormproject.dto.enrollment;

import ru.larkin.ormproject.entity.EnrollmentStatus;
import java.time.LocalDate;

public record EnrollmentResponseDto(Long id, Integer studentId, Integer courseId, EnrollmentStatus status, LocalDate enrolledAt) {}

