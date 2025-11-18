package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.larkin.ormproject.dto.enrollment.EnrollmentCreateDto;
import ru.larkin.ormproject.dto.enrollment.EnrollmentResponseDto;
import ru.larkin.ormproject.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> enroll(@RequestBody @Valid EnrollmentCreateDto dto) {
        EnrollmentResponseDto response = enrollmentService.enroll(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> unenroll(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        enrollmentService.unenroll(studentId, courseId);
        return ResponseEntity.noContent().build();
    }
}
