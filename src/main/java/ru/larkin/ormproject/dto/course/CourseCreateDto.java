package ru.larkin.ormproject.dto.course;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.dto.module.ModuleCreateDto;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDto {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private Integer teacherId;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Positive
    private Integer yearDuration;

    private Set<Integer> tagIds;

    @Valid
    private List<ModuleCreateDto> modules;
}
