package ru.larkin.ormproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larkin.ormproject.dto.lesson.LessonCreateDto;
import ru.larkin.ormproject.dto.lesson.LessonResponseDto;
import ru.larkin.ormproject.dto.lesson.LessonUpdateDto;
import ru.larkin.ormproject.entity.Lesson;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.LessonRepository;
import ru.larkin.ormproject.repository.ModuleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public LessonResponseDto createInModule(Long moduleId, LessonCreateDto dto) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> NotFoundException.forModule(moduleId));
        Lesson lesson = new Lesson();
        lesson.setTitle(dto.title());
        lesson.setDescription(dto.description());
        lesson.setLessonOrder(dto.lessonOrder());
        lesson.setVideoUrl(dto.videoUrl());
        lesson.setModule(module);
        Lesson saved = lessonRepository.save(lesson);
        return toResponse(saved);
    }

    @Transactional
    public LessonResponseDto update(Long id, LessonUpdateDto dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forLesson(id));
        if (dto.title() != null) lesson.setTitle(dto.title());
        if (dto.description() != null) lesson.setDescription(dto.description());
        if (dto.lessonOrder() != null) lesson.setLessonOrder(dto.lessonOrder());
        if (dto.videoUrl() != null) lesson.setVideoUrl(dto.videoUrl());
        if (dto.moduleId() != null) {
            Module module = moduleRepository.findById(dto.moduleId())
                    .orElseThrow(() -> NotFoundException.forModule(dto.moduleId()));
            lesson.setModule(module);
        }
        return toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public void delete(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forLesson(id));
        lessonRepository.delete(lesson);
    }

    public LessonResponseDto getLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forLesson(id));
        return toResponse(lesson);
    }

    public List<LessonResponseDto> getLessonsByModule(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LessonResponseDto toResponse(Lesson lesson) {
        return new LessonResponseDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getLessonOrder(),
                lesson.getVideoUrl(),
                lesson.getModule() != null ? lesson.getModule().getId() : null
        );
    }
}
