package ru.larkin.ormproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larkin.ormproject.dto.assignment.AssignmentCreateDto;
import ru.larkin.ormproject.dto.assignment.AssignmentResponseDto;
import ru.larkin.ormproject.entity.Assignment;
import ru.larkin.ormproject.entity.Lesson;
import ru.larkin.ormproject.repository.AssignmentRepository;
import ru.larkin.ormproject.repository.LessonRepository;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public AssignmentResponseDto createAssignment(@Valid AssignmentCreateDto dto) {
        Lesson lesson = lessonRepository.findById(dto.lessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson with id " + dto.lessonId() + " not found"));
        Assignment saved = assignmentRepository.save(convertToEntity(dto, lesson));
        return convertToDto(saved);
    }

    private Assignment convertToEntity(AssignmentCreateDto dto, Lesson lesson) {
        Assignment assignment = modelMapper.map(dto, Assignment.class);
        assignment.setLesson(lesson);
        return assignment;
    }

    private AssignmentResponseDto convertToDto(Assignment saved) {
        AssignmentResponseDto dto = modelMapper.map(saved, AssignmentResponseDto.class);
        dto.setLessonId(saved.getLesson().getId());
        return dto;
    }
}

