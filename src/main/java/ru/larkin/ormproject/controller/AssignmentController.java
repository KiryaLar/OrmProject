package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.larkin.ormproject.dto.assignment.AssignmentCreateDto;
import ru.larkin.ormproject.dto.assignment.AssignmentResponseDto;
import ru.larkin.ormproject.dto.submission.SubmissionResponseDto;
import ru.larkin.ormproject.entity.SubmissionStatus;
import ru.larkin.ormproject.service.AssignmentService;
import ru.larkin.ormproject.service.SubmissionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;


    @GetMapping
    public ResponseEntity<List<SubmissionResponseDto>> getSubmissions(
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) SubmissionStatus status
    ) {
        List<SubmissionResponseDto> submissions = submissionService.getSubmissions(assignmentId, studentId, status);
        return ResponseEntity.ok(submissions);
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestBody @Valid AssignmentCreateDto dto) {
        AssignmentResponseDto responseDto = assignmentService.createAssignment(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{assignmentId}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }
}

