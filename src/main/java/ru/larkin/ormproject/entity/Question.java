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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    @Column(nullable = false)
    private Integer maxScore;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> options = new ArrayList<>();
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Quiz quiz;
}
