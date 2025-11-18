package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {


}
