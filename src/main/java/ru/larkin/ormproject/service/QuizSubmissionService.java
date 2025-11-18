package ru.larkin.ormproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larkin.ormproject.dto.questionsubmission.QuestionSubmissionCreateDto;
import ru.larkin.ormproject.dto.quizsubmission.QuizSubmissionCreateDto;
import ru.larkin.ormproject.dto.quizsubmission.QuizSubmissionResponseDto;
import ru.larkin.ormproject.dto.selectedansweroption.SelectedAnswerOptionCreateDto;
import ru.larkin.ormproject.entity.*;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.AnswerOptionRepository;
import ru.larkin.ormproject.repository.QuestionRepository;
import ru.larkin.ormproject.repository.QuizRepository;
import ru.larkin.ormproject.repository.QuizSubmissionRepository;
import ru.larkin.ormproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizSubmissionService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Transactional
    public QuizSubmissionResponseDto submitQuiz(QuizSubmissionCreateDto dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> NotFoundException.forQuiz(dto.getQuizId()));

        User student = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> NotFoundException.forUser(dto.getUserId()));

        QuizSubmission quizSubmission = new QuizSubmission();
        quizSubmission.setQuiz(quiz);
        quizSubmission.setStudent(student);

        List<QuestionSubmission> questionSubmissions = new ArrayList<>();
        int totalScore = 0;

        if (dto.getQuestionSubmissions() != null) {
            for (QuestionSubmissionCreateDto questionDto : dto.getQuestionSubmissions()) {
                Question question = questionRepository.findById(questionDto.getQuestionId())
                        .orElseThrow(() -> NotFoundException.forQuestion(questionDto.getQuestionId()));

                QuestionSubmission questionSubmission = new QuestionSubmission();
                questionSubmission.setQuizSubmission(quizSubmission);
                questionSubmission.setQuestion(question);

                List<SelectedAnswerOption> selectedAnswerOptions = new ArrayList<>();
                Set<Long> selectedIds = new HashSet<>();
                if (questionDto.getSelectedOptions() != null) {
                    for (SelectedAnswerOptionCreateDto selectedDto : questionDto.getSelectedOptions()) {
                        AnswerOption option = answerOptionRepository.findById(selectedDto.getAnswerOptionId())
                                .orElseThrow(() -> NotFoundException.forAnswerOption(selectedDto.getAnswerOptionId()));
                        SelectedAnswerOption selectedAnswerOption = new SelectedAnswerOption();
                        selectedAnswerOption.setAnswerOption(option);
                        selectedAnswerOption.setQuestionSubmission(questionSubmission);
                        selectedAnswerOptions.add(selectedAnswerOption);
                        selectedIds.add(option.getId());
                    }
                }

                int questionScore = calculateScore(question, selectedIds);
                questionSubmission.setScore(questionScore);
                questionSubmission.setSelectedOptions(selectedAnswerOptions);

                totalScore += questionScore;
                questionSubmissions.add(questionSubmission);
            }
        }

        quizSubmission.setTotalScore(totalScore);
        quizSubmission.setQuestionSubmission(questionSubmissions);

        QuizSubmission saved = quizSubmissionRepository.save(quizSubmission);

        return QuizSubmissionResponseDto.builder()
                .id(saved.getId())
                .quizId(saved.getQuiz().getId())
                .userId(saved.getStudent().getId())
                .totalScore(saved.getTotalScore())
                .submitTime(saved.getSubmitTime())
                .build();
    }

    private int calculateScore(Question question, Set<Long> selectedOptionIds) {
        if (selectedOptionIds == null || selectedOptionIds.isEmpty()) {
            return 0;
        }

        List<AnswerOption> options = question.getOptions();
        Set<Long> correctIds = new HashSet<>();
        for (AnswerOption option : options) {
            if (Boolean.TRUE.equals(option.getIsCorrect())) {
                correctIds.add(option.getId());
            }
        }

        if (correctIds.isEmpty()) {
            return 0;
        }

        if (selectedOptionIds.equals(correctIds)) {
            return question.getMaxScore();
        }

        return 0;
    }
}
