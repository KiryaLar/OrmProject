package ru.larkin.ormproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larkin.ormproject.dto.submission.SubmissionResponseDto;
import ru.larkin.ormproject.entity.QuizSubmission;
import ru.larkin.ormproject.repository.QuizSubmissionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSubmissionRepository quizSubmissionRepository;

    public List<SubmissionResponseDto> getSubmissionsByStudent(Integer studentId) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findAllByStudent_Id(studentId);
        return submissions.stream()
                .map(this::toSubmissionResponseDto)
                .toList();
    }

    public List<SubmissionResponseDto> getSubmissionsByCourse(Integer courseId) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findAllByQuiz_Course_Id(courseId);
        return submissions.stream()
                .map(this::toSubmissionResponseDto)
                .toList();
    }

    public List<SubmissionResponseDto> getSubmissionsByModule(Long moduleId) {
        List<QuizSubmission> submissions = quizSubmissionRepository.findAllByQuiz_Module_Id(moduleId);
        return submissions.stream()
                .map(this::toSubmissionResponseDto)
                .toList();
    }

    private SubmissionResponseDto toSubmissionResponseDto(QuizSubmission submission) {
        SubmissionResponseDto dto = new SubmissionResponseDto();
        dto.setId(submission.getId());
        dto.setStudentId(submission.getStudent().getId());
        dto.setScore(submission.getTotalScore());
        dto.setSubmittedAt(submission.getSubmitTime());
        return dto;
    }
}
