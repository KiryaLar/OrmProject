package ru.larkin.ormproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.larkin.ormproject.dto.submission.SubmissionResponseDto;
import ru.larkin.ormproject.service.QuizService;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/submissions/by-student/{studentId}")
    public List<SubmissionResponseDto> getSubmissionsByStudent(@PathVariable Integer studentId) {
        return quizService.getSubmissionsByStudent(studentId);
    }

    @GetMapping("/submissions/by-course/{courseId}")
    public List<SubmissionResponseDto> getSubmissionsByCourse(@PathVariable Integer courseId) {
        return quizService.getSubmissionsByCourse(courseId);
    }

    @GetMapping("/submissions/by-module/{moduleId}")
    public List<SubmissionResponseDto> getSubmissionsByModule(@PathVariable Long moduleId) {
        return quizService.getSubmissionsByModule(moduleId);
    }
}
