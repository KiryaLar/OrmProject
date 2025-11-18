package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Enrollment;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndCourseId(Integer studentId, Integer courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
    List<Enrollment> findByCourseId(Integer courseId);
    List<Enrollment> findByStudentId(Integer studentId);
}
