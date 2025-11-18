package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Quiz quiz;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User student;
    private Integer totalScore;
    @CreatedDate
    private Instant submitTime;
    @OneToMany(mappedBy = "quizSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionSubmission> questionSubmission;
}
