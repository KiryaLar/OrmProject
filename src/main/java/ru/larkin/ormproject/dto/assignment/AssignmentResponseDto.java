package ru.larkin.ormproject.dto.assignment;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponseDto {
    private Long id;
    private String title;
    private String description;
    private Instant dueDate;
    private Integer maxScore;
    private Long lessonId;
}

