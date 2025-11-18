package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.larkin.ormproject.dto.course.CourseCreateDto;
import ru.larkin.ormproject.dto.course.CourseResponseDto;
import ru.larkin.ormproject.dto.course.CourseUpdateDto;
import ru.larkin.ormproject.dto.module.ModuleCreateDto;
import ru.larkin.ormproject.dto.module.ModuleResponseDto;
import ru.larkin.ormproject.dto.user.UserResponseDto;
import ru.larkin.ormproject.service.CourseService;
import ru.larkin.ormproject.service.ModuleService;
import ru.larkin.ormproject.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final ModuleService moduleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> allCourses() {
        List<CourseResponseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody @Valid CourseCreateDto courseCreateDto) {
        CourseResponseDto createdCourse = courseService.createCourse(courseCreateDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCourse.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Integer id,
            @RequestBody @Valid CourseUpdateDto dto
    ) {
        CourseResponseDto updated = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<ModuleResponseDto> createModule(
            @PathVariable Integer courseId,
            @RequestBody @Valid ModuleCreateDto dto
    ) {
        ModuleResponseDto created = moduleService.createInCourse(courseId, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/modules/{moduleId}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<ModuleResponseDto>> getCourseModules(@PathVariable Integer courseId) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<UserResponseDto>> getStudentsByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(userService.getStudentsByCourse(courseId));
    }
}
