package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToOne(cascade = CascadeType.ALL, optional = false,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Profile profile;
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseReview> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "teacher")
    private List<Course> coursesAsTeacher = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollmentAsStudent = new ArrayList<>();

    public void addCourse(Course course) {
        coursesAsTeacher.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        coursesAsTeacher.remove(course);
        course.setTeacher(null);
    }
}
