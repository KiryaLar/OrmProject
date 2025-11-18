package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Assignment;
import ru.larkin.ormproject.entity.Submission;
import ru.larkin.ormproject.entity.User;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>, JpaSpecificationExecutor<Submission> {

    boolean existsByStudentAndAssignment(User student, Assignment assignment);
}
