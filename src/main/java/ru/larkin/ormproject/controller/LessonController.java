package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.larkin.ormproject.dto.lesson.LessonResponseDto;
import ru.larkin.ormproject.dto.lesson.LessonUpdateDto;
import ru.larkin.ormproject.service.LessonService;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDto> getLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getLesson(lessonId));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDto> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody @Valid LessonUpdateDto dto
    ) {
        return ResponseEntity.ok(lessonService.update(lessonId, dto));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.delete(lessonId);
        return ResponseEntity.noContent().build();
    }
}
