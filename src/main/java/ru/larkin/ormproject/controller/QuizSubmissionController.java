package ru.larkin.ormproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.larkin.ormproject.dto.quizsubmission.QuizSubmissionCreateDto;
import ru.larkin.ormproject.dto.quizsubmission.QuizSubmissionResponseDto;
import ru.larkin.ormproject.service.QuizSubmissionService;

@RestController
@RequestMapping("/api/quiz-submissions")
@RequiredArgsConstructor
public class QuizSubmissionController {
    private final QuizSubmissionService quizSubmissionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuizSubmissionResponseDto submitQuiz(@Valid @RequestBody QuizSubmissionCreateDto dto) {
        return quizSubmissionService.submitQuiz(dto);
    }
}
