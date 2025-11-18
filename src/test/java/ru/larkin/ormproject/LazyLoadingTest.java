package ru.larkin.ormproject;

import jakarta.persistence.EntityManager;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.repository.*;
import ru.larkin.ormproject.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.larkin.ormproject.utils.CourseLazyService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LazyLoadingTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CourseLazyService courseLazyService;

    @Test
    void accessingLazyCollectionOutsideTransaction_throwsLazyInitializationException() {
        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(2000, 1, 1));

        User teacher = new User();
        teacher.setProfile(profile);
        teacher.setEmail("fake-teacher@example.com");
        teacher.setName("Teacher");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Lazy test course");
        course.setDescription("desc");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        Module m1 = new Module();
        m1.setTitle("Module 1");
        m1.setModuleOrder(1);

        Module m2 = new Module();
        m2.setTitle("Module 2");
        m2.setModuleOrder(2);

        course.addModule(m1);
        course.addModule(m2);

        Course saved = courseRepository.save(course);
        Integer courseId = saved.getId();

        // ---------- act: грузим курс в транзакции сервиса ----------
        Course detachedCourse = courseLazyService.loadCourse(courseId);
        // здесь транзакция, открытая в сервисе, уже ЗАКРЫТА

        // ---------- assert: ленивый доступ за пределами транзакции ----------
        assertThrows(
                org.hibernate.LazyInitializationException.class,
                () -> detachedCourse.getModules().size()
        );
    }
}
