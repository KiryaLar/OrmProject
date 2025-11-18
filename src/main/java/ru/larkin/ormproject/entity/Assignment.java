package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private Instant dueDate;
    @Column(nullable = false)
    private Integer maxScore;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Lesson lesson;
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    @OrderBy("submittedAt DESC")
    private List<Submission> submissions = new ArrayList<>();

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        submission.setAssignment(this);
    }

    public void removeSubmission(Submission submission) {
        submissions.remove(submission);
        submission.setAssignment(null);
    }
}
