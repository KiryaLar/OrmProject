package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.larkin.ormproject.dto.lesson.LessonCreateDto;
import ru.larkin.ormproject.dto.lesson.LessonResponseDto;
import ru.larkin.ormproject.dto.module.ModuleResponseDto;
import ru.larkin.ormproject.dto.module.ModuleUpdateDto;
import ru.larkin.ormproject.service.LessonService;
import ru.larkin.ormproject.service.ModuleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;
    private final LessonService lessonService;

    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponseDto> updateModule(
            @PathVariable Long id,
            @RequestBody @Valid ModuleUpdateDto dto
    ) {
        ModuleResponseDto updated = moduleService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{moduleId}/lessons")
    public ResponseEntity<LessonResponseDto> createLesson(
            @PathVariable Long moduleId,
            @RequestBody @Valid LessonCreateDto dto
    ) {
        LessonResponseDto created = lessonService.createInModule(moduleId, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/lessons/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDto> getModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.getModule(moduleId));
    }

    @GetMapping("/{moduleId}/lessons")
    public ResponseEntity<List<LessonResponseDto>> getModuleLessons(@PathVariable Long moduleId) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId));
    }
}
