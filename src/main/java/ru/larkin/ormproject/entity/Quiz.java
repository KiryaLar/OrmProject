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
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Enumerated(EnumType.STRING)
    private QuizType quizType;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer maxScore;
    @Column(nullable = false)
    private Instant startDate;
    @Column(nullable = false)
    private Integer durationInMinutes;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Course course;
    @OneToOne
    @JoinColumn(unique = true)
    private Module module;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> question = new ArrayList<>();
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();
}

