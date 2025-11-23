package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.larkin.ormproject.dto.course.CourseResponseDto;
import ru.larkin.ormproject.dto.user.UserCreateDto;
import ru.larkin.ormproject.dto.user.UserResponseDto;
import ru.larkin.ormproject.entity.Role;
import ru.larkin.ormproject.service.CourseService;
import ru.larkin.ormproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserResponseDto>> getAllStudents() {
        return ResponseEntity.ok(userService.getByRole(Role.STUDENT));
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(courseService.getCoursesByStudent(studentId));
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserResponseDto>> getAllTeachers() {
        return ResponseEntity.ok(userService.getByRole(Role.TEACHER));
    }

    @PostMapping("/students")
    public ResponseEntity<UserResponseDto> createStudent(@RequestBody @Valid UserCreateDto dto) {
        dto.setRole(Role.STUDENT);
        UserResponseDto created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/teachers")
    public ResponseEntity<UserResponseDto> createTeacher(@RequestBody @Valid UserCreateDto dto) {
        dto.setRole(Role.TEACHER);
        UserResponseDto created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
