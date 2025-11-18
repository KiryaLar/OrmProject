package ru.larkin.ormproject.dto.course;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDto {
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    private Integer teacherId;

    private Integer categoryId;

    @Positive
    private Integer yearDuration;

    private Set<Integer> tagIds;
}
