package ru.larkin.ormproject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class CourseReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User student;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Course course;
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;
    @Column(length = 1000)
    private String comment;
    @CreatedDate
    private Instant createdAt;
}
