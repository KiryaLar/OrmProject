package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"question_submission_id", "answer_option_id"}
        )
)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SelectedAnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private UUID uid = UUID.randomUUID();
    @ManyToOne(optional = false)
    private QuestionSubmission questionSubmission;
    @ManyToOne(optional = false)
    private AnswerOption answerOption;
}
