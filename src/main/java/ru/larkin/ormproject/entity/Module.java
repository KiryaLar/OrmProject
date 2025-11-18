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
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer moduleOrder;
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("lessonOrder ASC")
    private List<Lesson> lessons = new ArrayList<>();
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Course course;
    @OneToOne(mappedBy = "module")
    private Quiz quiz;

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setModule(this);
    }
}
