package ru.larkin.ormproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larkin.ormproject.dto.enrollment.EnrollmentCreateDto;
import ru.larkin.ormproject.dto.enrollment.EnrollmentResponseDto;
import ru.larkin.ormproject.entity.*;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.CourseRepository;
import ru.larkin.ormproject.repository.EnrollmentRepository;
import ru.larkin.ormproject.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrollmentResponseDto enroll(EnrollmentCreateDto dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> NotFoundException.forUser(dto.getStudentId()));
        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Only users with role STUDENT can be enrolled to courses");
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> NotFoundException.forCourse(dto.getCourseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalStateException("Student is already enrolled to this course");
        }

        Enrollment enrollment = constructEntity(student, course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToResponse(saved);
    }

    @Transactional
    public void unenroll(Integer studentId, Integer courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> NotFoundException.forEnrollment(studentId, courseId));
        enrollmentRepository.delete(enrollment);
    }

    private Enrollment constructEntity(User student, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        return enrollment;
    }

    private EnrollmentResponseDto convertToResponse(Enrollment enrollment) {
        return new EnrollmentResponseDto(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt()
        );
    }
}
