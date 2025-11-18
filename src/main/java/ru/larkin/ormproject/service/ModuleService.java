package ru.larkin.ormproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.larkin.ormproject.dto.module.ModuleCreateDto;
import ru.larkin.ormproject.dto.module.ModuleResponseDto;
import ru.larkin.ormproject.dto.module.ModuleUpdateDto;
import ru.larkin.ormproject.entity.Course;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.CourseRepository;
import ru.larkin.ormproject.repository.ModuleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ModuleResponseDto createInCourse(Integer courseId, ModuleCreateDto dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> NotFoundException.forCourse(courseId));

        Module module = modelMapper.map(dto, Module.class);
        module.setCourse(course);

        Module saved = moduleRepository.save(module);
        return convertToResponse(saved);
    }

    @Transactional
    public ModuleResponseDto update(Long id, ModuleUpdateDto dto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forModule(id));
        if (dto.title() != null) module.setTitle(dto.title());
        if (dto.moduleOrder() != null) module.setModuleOrder(dto.moduleOrder());
        if (dto.courseId() != null) {
            Course newCourse = courseRepository.findById(dto.courseId())
                    .orElseThrow(() -> NotFoundException.forCourse(dto.courseId()));
            module.setCourse(newCourse);
        }
        return convertToResponse(moduleRepository.save(module));
    }

    @Transactional
    public void delete(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forModule(id));
        moduleRepository.delete(module);
    }

    public List<ModuleResponseDto> getModulesByCourse(Integer courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ModuleResponseDto getModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forModule(id));
        return convertToResponse(module);
    }

    private ModuleResponseDto convertToResponse(Module module) {
        return new ModuleResponseDto(
                module.getId(),
                module.getTitle(),
                module.getModuleOrder(),
                module.getCourse() != null ? module.getCourse().getId() : null
        );
    }
}
