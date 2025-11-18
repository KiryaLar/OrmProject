package ru.larkin.ormproject;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ru.larkin.ormproject.entity.*;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AssignmentSubmissionJpaIntegrationTest {

    @Autowired UserRepository userRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired AssignmentRepository assignmentRepository;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired EntityManager em;

    /**
     * CREATE + READ: создаём assignment и submission, проверяем связи.
     */
    @Test
    @Transactional
    void createAssignmentAndSubmission_checkRelations() {
        // teacher
        User teacher = createStudent("fake-teacher@example.com");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        // course + lesson
        Course course = createSimpleCourse(teacher);
        Lesson lesson = createLessonForCourse(course);

        // assignment
        Assignment assignment = new Assignment();
        assignment.setTitle("Implement LinkedList");
        assignment.setDescription("Write your own LinkedList implementation.");
        assignment.setMaxScore(100);
        assignment.setDueDate(OffsetDateTime.of(2025, 3, 1,
                23, 59, 0, 0, ZoneOffset.UTC)
                .toInstant());
        assignment.setLesson(lesson);

        // student + enrollment
        User student = createStudent("student-assign@example.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDate.of(2025, 1, 15));
        enrollment = enrollmentRepository.save(enrollment);

        // submission (через связь assignment -> submissions)
        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setContent("My LinkedList implementation: https://github.com/...");
        submission.setStatus(SubmissionStatus.PENDING);
        submission.setSubmittedAt(Instant.parse("2025-02-10T20:00:00Z"));

        assignment.addSubmission(submission);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        em.flush();
        em.clear();

        // READ: проверяем из базы
        Assignment fromDb = assignmentRepository.findById(savedAssignment.getId()).orElseThrow();
        assertThat(fromDb.getLesson().getId()).isEqualTo(lesson.getId());
        assertThat(fromDb.getSubmissions()).hasSize(1);

        Submission fromDbSub = fromDb.getSubmissions().getFirst();
        assertThat(fromDbSub.getStudent().getId()).isEqualTo(student.getId());
        assertThat(fromDbSub.getContent()).contains("LinkedList");
        assertThat(fromDbSub.getStatus()).isEqualTo(SubmissionStatus.PENDING);

        Enrollment fromDbEnrollment = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        assertThat(fromDbEnrollment.getCourse().getId()).isEqualTo(course.getId());
        assertThat(fromDbEnrollment.getStudent().getId()).isEqualTo(student.getId());
        assertThat(fromDbEnrollment.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
    }

    /**
     * UPDATE: ставим оценку submission и меняем статус, убеждаемся, что изменения записаны.
     */
    @Test
    @Transactional
    void updateSubmission_setScoreAndStatus() {
        User teacher = createStudent("fake-teacher2@example.com");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = createSimpleCourse(teacher);
        Lesson lesson = createLessonForCourse(course);

        Assignment assignment = new Assignment();
        assignment.setTitle("Simple task");
        assignment.setMaxScore(100);
        assignment.setDueDate(OffsetDateTime.now().plusDays(7).toInstant());
        assignment.setLesson(lesson);

        User student = createStudent("student-update@example.com");

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setContent("Initial content");
        submission.setStatus(SubmissionStatus.PENDING);

        assignment.addSubmission(submission);

        assignment = assignmentRepository.save(assignment);
        em.flush();
        em.clear();

        Submission persisted = assignmentRepository.findById(assignment.getId())
                .orElseThrow()
                .getSubmissions()
                .getFirst();

        // ACT: оценка и статус
        persisted.setScore(85);
        persisted.setStatus(SubmissionStatus.ACCEPTED);
        persisted.setFeedback("Good job, minor issues.");
        submissionRepository.save(persisted);
        em.flush();
        em.clear();

        Submission reloaded = submissionRepository.findById(persisted.getId()).orElseThrow();
        assertThat(reloaded.getScore()).isEqualTo(85);
        assertThat(reloaded.getStatus()).isEqualTo(SubmissionStatus.ACCEPTED);
        assertThat(reloaded.getFeedback()).contains("Good job");
    }

    /**
     * DELETE: удаляем assignment и проверяем, что submissions тоже удалились (если orphanRemoval = true).
     */
    @Test
    @Transactional
    void deleteAssignment_cascadeDeleteSubmissions() {
        User teacher = createStudent("fake-teacher3@example.com");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = createSimpleCourse(teacher);
        Lesson lesson = createLessonForCourse(course);

        Assignment assignment = new Assignment();
        assignment.setTitle("Task to delete");
        assignment.setMaxScore(100);
        assignment.setDueDate(OffsetDateTime.now().plusDays(3).toInstant());
        assignment.setLesson(lesson);

        User student = createStudent("student-delete-sub@example.com");

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setContent("Work to be deleted");
        submission.setStatus(SubmissionStatus.PENDING);

        assignment.addSubmission(submission);

        assignment = assignmentRepository.save(assignment);
        em.flush();

        Long submissionId = assignment.getSubmissions().getFirst().getId();

        // ACT: удаляем assignment
        assignmentRepository.delete(assignment);
        em.flush();
        em.clear();

        // ASSERT: submission тоже исчез (при orphanRemoval = true)
        assertThat(submissionRepository.findById(submissionId)).isEmpty();
    }

    private User createStudent(String email) {
        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(2000, 1, 1));
        profile = profileRepository.save(profile);

        User student = new User();
        student.setProfile(profile);
        student.setEmail(email);
        student.setName("Student " + email);
        student.setRole(Role.STUDENT);
        return userRepository.save(student);
    }

    private Course createSimpleCourse(User teacher) {
        Category category = new Category();
        category.setName("TestCourseCategory");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("For assignment/submission tests");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        return courseRepository.save(course);
    }

    private Lesson createLessonForCourse(Course course) {
        Module module = new Module();
        module.setTitle("Module for assignments");
        module.setModuleOrder(1);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson with homework");
        lesson.setDescription("Lesson having an assignment");
        lesson.setLessonOrder(1);

        module.addLesson(lesson);
        course.addModule(module);

        courseRepository.save(course);
        em.flush();
        em.clear();

        Course reloaded = courseRepository.findById(course.getId()).orElseThrow();
        return reloaded.getModules().getFirst().getLessons().getFirst();
    }
}
