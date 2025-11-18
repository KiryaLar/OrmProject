package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.QuizSubmission;

import java.util.List;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findAllByStudent_Id(Integer studentId);
    List<QuizSubmission> findAllByQuiz_Course_Id(Integer courseId);
    List<QuizSubmission> findAllByQuiz_Module_Id(Long moduleId);
}
