package ru.larkin.ormproject;

import ru.larkin.ormproject.entity.*;
import ru.larkin.ormproject.entity.Module;
import ru.larkin.ormproject.repository.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseJpaIntegrationTest {

    @Autowired CategoryRepository categoryRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ModuleRepository moduleRepository;
    @Autowired LessonRepository lessonRepository;
    @Autowired EntityManager em;

    /**
     * CREATE: сохраняем курс с модулями и уроками, проверяем каскадный persist.
     */
    @Test
    @Transactional
    void createCourse_withModulesAndLessons_cascadePersist() {
        // --- подготовка учителя и категории ---
        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(1985, 3, 10));
        profile = profileRepository.save(profile);

        User teacher = new User();
        teacher.setProfile(profile);
        teacher.setEmail("teacher-it@example.com");
        teacher.setName("Ivanov Ivan");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Integration Testing");
        category = categoryRepository.save(category);

        // --- создаём курс ---
        Course course = new Course();
        course.setTitle("Integration Test Course");
        course.setDescription("Course used for JPA integration tests.");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        // модуль 1
        Module m1 = new Module();
        m1.setTitle("Module 1");
        m1.setModuleOrder(1);

        // уроки в модуле 1
        Lesson l11 = new Lesson();
        l11.setTitle("Lesson 1.1");
        l11.setDescription("Intro lesson");
        l11.setLessonOrder(1);

        Lesson l12 = new Lesson();
        l12.setTitle("Lesson 1.2");
        l12.setDescription("Second lesson");
        l12.setLessonOrder(2);

        m1.addLesson(l11);
        m1.addLesson(l12);

        // модуль 2
        Module m2 = new Module();
        m2.setTitle("Module 2");
        m2.setModuleOrder(2);

        course.addModule(m1);
        course.addModule(m2);

        // --- ACT: сохраняем только курс ---
        Course saved = courseRepository.save(course);
        em.flush();
        em.clear();

        // --- ASSERT: всё каскадно сохранилось ---
        Course reloaded = courseRepository.findById(saved.getId()).orElseThrow();

        assertThat(reloaded.getModules()).hasSize(2);

        Module reloadedM1 = reloaded.getModules().stream()
                .filter(m -> m.getModuleOrder() == 1)
                .findFirst()
                .orElseThrow();

        assertThat(reloadedM1.getLessons()).hasSize(2);
        assertThat(reloadedM1.getLessons())
                .extracting(Lesson::getTitle)
                .containsExactlyInAnyOrder("Lesson 1.1", "Lesson 1.2");
    }

    /**
     * READ: читаем курс, проверяем связи с модулем и категорией / учителем.
     */
    @Test
    @Transactional
    void readCourse_checkRelations() {
        // создаём курс (можно использовать такой же сетап, как выше)
        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(1990, 1, 1));
        profile = profileRepository.save(profile);

        User teacher = new User();
        teacher.setProfile(profile);
        teacher.setEmail("teacher-read@example.com");
        teacher.setName("Read Teacher");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Programming");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Reading Course");
        course.setDescription("Course for reading test");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        Module module = new Module();
        module.setTitle("Only module");
        module.setModuleOrder(1);
        course.addModule(module);

        Course saved = courseRepository.save(course);
        em.flush();
        em.clear();

        // ACT
        Course fromDb = courseRepository.findById(saved.getId()).orElseThrow();

        // ASSERT
        assertThat(fromDb.getTeacher().getEmail()).isEqualTo("teacher-read@example.com");
        assertThat(fromDb.getCategory().getName()).isEqualTo("Programming");
        assertThat(fromDb.getModules()).hasSize(1);
        assertThat(fromDb.getModules().getFirst().getTitle()).isEqualTo("Only module");
    }

    /**
     * UPDATE: обновляем описание курса, проверяем, что изменения сохранились.
     */
    @Test
    @Transactional
    void updateCourse_descriptionIsUpdated() {
        Category category = new Category();
        category.setName("Super Category");
        category = categoryRepository.save(category);

        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(1980, 5, 5));
        profile = profileRepository.save(profile);

        User teacher = new User();
        teacher.setProfile(profile);
        teacher.setEmail("teacher-update@example.com");
        teacher.setName("Kirill Kirillov");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Update Course");
        course.setDescription("Old description");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        Course saved = courseRepository.save(course);
        em.flush();
        em.clear();

        // ACT: меняем описание
        Course toUpdate = courseRepository.findById(saved.getId()).orElseThrow();
        toUpdate.setDescription("New updated description");
        courseRepository.save(toUpdate);
        em.flush();
        em.clear();

        // ASSERT
        Course updated = courseRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getDescription()).isEqualTo("New updated description");
    }

    /**
     * DELETE: удаляем курс и проверяем, что модули и уроки удалились (если стоит cascade + orphanRemoval).
     */
    @Test
    @Transactional
    void deleteCourse_cascadeDeleteModulesAndLessons() {
        // подготовим структуру
        Profile profile = new Profile();
        profile.setBirthDate(LocalDate.of(1995, 1, 1));
        profile = profileRepository.save(profile);

        User teacher = new User();
        teacher.setProfile(profile);
        teacher.setEmail("teacher-delete@example.com");
        teacher.setName("Delete Teacher");
        teacher.setRole(Role.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Super Category");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Delete Course");
        course.setDescription("To be deleted");
        course.setYearDuration(1);
        course.setTeacher(teacher);
        course.setCategory(category);

        Module module = new Module();
        module.setTitle("Module to delete");
        module.setModuleOrder(1);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson to delete");
        lesson.setDescription("Should be removed with course");
        lesson.setLessonOrder(1);

        module.addLesson(lesson);
        course.addModule(module);

        Course saved = courseRepository.save(course);
        em.flush();

        Long moduleId = saved.getModules().getFirst().getId();
        Long lessonId = saved.getModules().getFirst().getLessons().getFirst().getId();

        // ACT: удаляем курс
        courseRepository.delete(saved);
        em.flush();
        em.clear();

        // ASSERT: модуль и урок должны исчезнуть (если orphanRemoval = true)
        assertThat(moduleRepository.findById(moduleId)).isEmpty();
        assertThat(lessonRepository.findById(lessonId)).isEmpty();
    }
}
