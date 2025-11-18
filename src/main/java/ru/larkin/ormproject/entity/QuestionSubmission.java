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
//@Builder
public class QuestionSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Question question;
    private Integer score;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private QuizSubmission quizSubmission;
    @OneToMany(mappedBy = "questionSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedAnswerOption> selectedOptions = new ArrayList<>();
}
