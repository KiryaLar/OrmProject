package ru.larkin.ormproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid = UUID.randomUUID();
    @Column(nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User teacher;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("moduleOrder ASC")
    private List<Module> modules = new ArrayList<>();
    @Column(nullable = false)
    private Integer yearDuration;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseReview> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> quizzes = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "course_tag",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public void addModule(Module module) {
        modules.add(module);
        module.setCourse(this);
    }
}
