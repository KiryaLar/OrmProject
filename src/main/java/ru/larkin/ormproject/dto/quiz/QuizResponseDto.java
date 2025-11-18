package ru.larkin.ormproject.dto.quiz;

import ru.larkin.ormproject.entity.QuizType;
import java.time.Instant;

public record QuizResponseDto(Long id, QuizType quizType, String title, Integer maxScore, Instant startDate, Integer durationInMinutes, Integer courseId, Long moduleId) {}

