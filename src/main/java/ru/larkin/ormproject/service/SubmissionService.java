package ru.larkin.ormproject.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larkin.ormproject.dto.GradeSubmissionDto;
import ru.larkin.ormproject.dto.submission.SubmissionCreateDto;
import ru.larkin.ormproject.dto.submission.SubmissionResponseDto;
import ru.larkin.ormproject.entity.Assignment;
import ru.larkin.ormproject.entity.Submission;
import ru.larkin.ormproject.entity.SubmissionStatus;
import ru.larkin.ormproject.entity.User;
import ru.larkin.ormproject.repository.AssignmentRepository;
import ru.larkin.ormproject.repository.SubmissionRepository;
import ru.larkin.ormproject.repository.UserRepository;

import java.util.List;

import static ru.larkin.ormproject.specification.SubmissionSpecifications.*;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public SubmissionResponseDto createSubmission(@Valid SubmissionCreateDto dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + dto.getStudentId() + " not found"));
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment with id " + dto.getAssignmentId() + " not found"));

        if (submissionRepository.existsByStudentAndAssignment(student, assignment)) {
            throw new IllegalStateException("Student with id " + dto.getStudentId() +
                                            " has already submitted a solution for assignment " + dto.getAssignmentId());
        }

        Submission saved = submissionRepository.save(convertToEntity(dto, student, assignment));
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponseDto> getSubmissions(Long assignmentId, Integer studentId, SubmissionStatus status) {
        Specification<Submission> spec = Specification.allOf(
                byAssignment(assignmentId),
                byStudent(studentId),
                byStatus(status)
        );
        return submissionRepository.findAll(spec).stream()
                .map(this::convertToDto)
                .toList();
    }

    public SubmissionResponseDto gradeSubmission(Long submissionId, @Valid GradeSubmissionDto grade) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission with id " + submissionId + " not found"));
        submission.setStatus(SubmissionStatus.ACCEPTED);
        submission.setScore(grade.getScore());
        submission.setFeedback(grade.getFeedback());
        Submission updated = submissionRepository.save(submission);
        return convertToDto(updated);
    }

    private Submission convertToEntity(@Valid SubmissionCreateDto dto, User student, Assignment assignment) {
        return Submission.builder()
                .student(student)
                .assignment(assignment)
                .content(dto.getContent())
                .status(SubmissionStatus.PENDING)
                .build();
    }

    private SubmissionResponseDto convertToDto(Submission submission) {
        SubmissionResponseDto responseDto = modelMapper.map(submission, SubmissionResponseDto.class);
        responseDto.setAssignmentId(submission.getAssignment().getId());
        responseDto.setStudentId(submission.getStudent().getId());
        return responseDto;
    }
}

