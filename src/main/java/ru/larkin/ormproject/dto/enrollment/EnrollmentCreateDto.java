package ru.larkin.ormproject.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCreateDto {
    @NotNull
    private Integer studentId;

    @NotNull
    private Integer courseId;
}
