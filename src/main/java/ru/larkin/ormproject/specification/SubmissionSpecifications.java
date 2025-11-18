package ru.larkin.ormproject.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.larkin.ormproject.entity.Submission;
import ru.larkin.ormproject.entity.SubmissionStatus;

@UtilityClass
public class SubmissionSpecifications {

    public static Specification<Submission> byAssignment(Long assignmentId) {
        return (root, query, criteriaBuilder) -> {
            if (assignmentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("assignment").get("id"), assignmentId);
        };
    }

    public static Specification<Submission> byStudent(Integer studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("student").get("id"), studentId);
        };
    }

    public static Specification<Submission> byStatus(SubmissionStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
