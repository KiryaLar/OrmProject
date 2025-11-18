package ru.larkin.ormproject.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larkin.ormproject.dto.course.CourseCreateDto;
import ru.larkin.ormproject.dto.course.CourseResponseDto;
import ru.larkin.ormproject.dto.course.CourseUpdateDto;
import ru.larkin.ormproject.dto.lesson.LessonCreateDto;
import ru.larkin.ormproject.dto.module.ModuleCreateDto;
import ru.larkin.ormproject.entity.*;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public CourseResponseDto createCourse(CourseCreateDto courseCreateDto) {

        User teacher = userRepository.findByIdAndRole(courseCreateDto.getTeacherId(), Role.TEACHER)
                .orElseThrow(() -> NotFoundException.forTeacher(courseCreateDto.getCategoryId()));

        Category category = categoryRepository.findById(courseCreateDto.getCategoryId())
                .orElseThrow(() -> NotFoundException.forCategory(courseCreateDto.getCategoryId()));

        Course course = convertToEntity(courseCreateDto);
        course.setTeacher(teacher);
        course.setCategory(category);

        createModulesIfExists(courseCreateDto.getModules(), course);

        Course createdCourse = courseRepository.save(course);
        return convertToDto(createdCourse);
    }

    @Transactional
    public CourseResponseDto updateCourse(Integer id, CourseUpdateDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forCourse(id));

        if (dto.getTitle() != null) course.setTitle(dto.getTitle());
        if (dto.getDescription() != null) course.setDescription(dto.getDescription());
        if (dto.getYearDuration() != null) course.setYearDuration(dto.getYearDuration());

        if (dto.getTeacherId() != null) {
            User teacher = userRepository.findByIdAndRole(dto.getTeacherId(), Role.TEACHER)
                    .orElseThrow(() -> NotFoundException.forTeacher(dto.getTeacherId()));
            course.setTeacher(teacher);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> NotFoundException.forCategory(dto.getCategoryId()));
            course.setCategory(category);
        }

        if (dto.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            course.setTags(tags);
        }

        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    public List<CourseResponseDto> getCoursesByStudent(Integer studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(Enrollment::getCourse)
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .toList();
    }

    private CourseResponseDto convertToDto(Course course) {
        CourseResponseDto dto = modelMapper.map(course, CourseResponseDto.class);
        dto.setTeacherId(course.getTeacher().getId());
        dto.setCategoryId(course.getCategory().getId());
        return dto;
    }

    private Course convertToEntity(CourseCreateDto courseCreateDto) {
        return modelMapper.map(courseCreateDto, Course.class);
    }

    private void createModulesIfExists(List<ModuleCreateDto> maybeModules, Course course) {
        if (maybeModules == null || maybeModules.isEmpty()) {
            return;
        }
        maybeModules
                .forEach(moduleDto -> {
                    Module module = modelMapper.map(moduleDto, Module.class);
                    course.addModule(module);
                    createLessonsIfExists(moduleDto.lessons(), module);
                });
    }

    private void createLessonsIfExists(List<LessonCreateDto> maybeLessons, Module module) {
        if (maybeLessons == null || maybeLessons.isEmpty()) {
            return;
        }
        maybeLessons
                .forEach(lessonDto -> {
                    Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
                    module.addLesson(lesson);
                });
    }

}
