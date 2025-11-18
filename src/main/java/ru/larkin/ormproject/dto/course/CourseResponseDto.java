package ru.larkin.ormproject.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.dto.tag.TagResponseDto;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {
    private Integer id;
    private UUID uuid;
    private String title;
    private String description;
    private Integer teacherId;
    private Integer categoryId;
    private Integer yearDuration;
    private Set<TagResponseDto> tags;
}
