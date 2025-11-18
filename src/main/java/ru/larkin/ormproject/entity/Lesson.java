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
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer lessonOrder;
    private String videoUrl;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Module module;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Assignment> assignment = new ArrayList<>();
}
