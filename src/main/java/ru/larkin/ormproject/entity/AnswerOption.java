package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Boolean isCorrect;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Question question;
    @OneToMany(mappedBy = "answerOption")
    private Set<SelectedAnswerOption> selectedAnswerOptions = new HashSet<>();
}
