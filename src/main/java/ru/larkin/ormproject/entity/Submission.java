package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;
    @Column(nullable = false, length = 1000)
    private String content;
    private Integer score;
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;
    @Column(length = 1000)
    private String feedback;
    @CreatedDate
    @Column(updatable = false)
    private Instant submittedAt;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Assignment assignment;
}
