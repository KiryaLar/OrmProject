package ru.larkin.ormproject.dto.lesson;

public record LessonResponseDto(Long id, String title, String description, Integer lessonOrder, String videoUrl, Long moduleId) {}

