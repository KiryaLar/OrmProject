package ru.larkin.ormproject.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larkin.ormproject.entity.Course;
import ru.larkin.ormproject.repository.CourseRepository;

@Service
public class CourseLazyService {

    private final CourseRepository courseRepository;

    public CourseLazyService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Course loadCourse(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
