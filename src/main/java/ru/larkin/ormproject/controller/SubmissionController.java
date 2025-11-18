package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.larkin.ormproject.dto.GradeSubmissionDto;
import ru.larkin.ormproject.dto.submission.SubmissionCreateDto;
import ru.larkin.ormproject.dto.submission.SubmissionResponseDto;
import ru.larkin.ormproject.service.SubmissionService;

import java.net.URI;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionResponseDto> createSubmission(@RequestBody @Valid SubmissionCreateDto dto) {
        SubmissionResponseDto responseDto = submissionService.createSubmission(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{submissionId}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @PatchMapping("/{submissionId}/grade")
    public ResponseEntity<SubmissionResponseDto> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody @Valid GradeSubmissionDto grade
    ) {
        SubmissionResponseDto responseDto = submissionService.gradeSubmission(submissionId, grade);
        return ResponseEntity.ok(responseDto);
    }
}

