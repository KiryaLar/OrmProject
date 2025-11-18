# Online Courses Platform (ORM Project)

Учебный проект системы управления онлайн-курсами на базе **Spring Boot + Spring Data JPA + PostgreSQL**.

Позволяет:

- создавать курсы с категорией, преподавателем, модулями и уроками;
- записывать студентов на курсы;
- выдавать задания и принимать решения;
- проводить тесты/викторины (quiz), хранить результаты;
- оставлять отзывы на курсы.

---

## 1. Стек технологий

- **Java 17+**
- **Spring Boot** (Web, Data JPA, Validation)
- **PostgreSQL 17** (основная БД)
- **Gradle**
- **Lombok** (геттеры/сеттеры, конструкторы)
- **ModelMapper** (маппинг Entity ⇄ DTO)
- **JUnit 5, Spring Test** — для unit + интеграционных тестов
- **H2** (in-memory для интеграционных тестов)

---

## 2. Модель данных

Всего 18 сущностей:

- **User** — пользователь платформы (student / teacher)
- **Profile** — профиль пользователя (1–1 с User)
- **Category** — категория курса (Programming, Math, …)
- **Tag** — тег курса (Java, Spring, Beginner)
- **Course** — курс
- **CourseReview** — отзыв студента о курсе
- **Enrollment** — запись студента на курс (Many-to-Many через сущность)
- **Module** — модуль курса
- **Lesson** — урок в модуле
- **Assignment** — задание к уроку
- **Submission** — решение задания
- **Quiz** — тест/викторина
- **Question** — вопрос в квизе
- **AnswerOption** — вариант ответа
- **QuizSubmission** — попытка прохождения квиза студентом
- **QuestionSubmission** — ответ на конкретный вопрос в рамках попытки
- **SelectedAnswerOption** — выбранный вариант ответа
- **Tag** - теги курсов

Типы связей:

- **1–1**
    - `User` — `Profile`
- **1–Many**
    - `User (teacher)` — `Course`
    - `Course` — `Module`
    - `Module` — `Lesson`
    - `Lesson` — `Assignment`
    - `Assignment` — `Submission`
    - `Quiz` — `Question`
    - `Question` — `AnswerOption`
    - `QuizSubmission` — `QuestionSubmission`
    - `QuestionSubmission` — `SelectedAnswerOption`
    - `Course` — `CourseReview`
- **Many–Many**
    - `Course` ⟷ `Tag` (через `course_tag`)
    - `Course` ⟷ `User (student)` (через `Enrollment` как полноценную сущность)

Связи спроектированы так, чтобы:

- не использовать «голое» `@ManyToMany` там, где нужна бизнес-информация (Enrollment со статусом и датой);
- правильно работать с каскадами и `orphanRemoval` для зависимых сущностей (модули, уроки, сабмишены).

---

## 3. Структура проекта (архитектура)

Условная структура пакетов:

```text
src/main/java/ru/…/ormproject
 ├─ entity/          # JPA-сущности
 ├─ repository/      # Spring Data JPA репозитории
 ├─ dto/             # DTO-объекты для запросов/ответов
 ├─ service/         # Бизнес-логика (CourseService, EnrollmentService, ...)
 ├─ controller/      # REST-контроллеры
 ├─ exception/       # NotFoundException, валидаторы и обработчик ошибок
 ├─ config/          # Конфигурация (Spring, Jackson, Swagger при наличии)
 └─ specifiacation/  # Классы для динамического построения запросов (JPA Criteria API)
```

## 4. Конфигурация приложения и профили

Используются профили:

- dev — локальная разработка с PostgreSQL;
- test — тесты (H2 in memory).

Пример src/main/resources/application-dev.yml:
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/postgres }
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
```

Пример src/test/resources/application-test.yml:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
  sql:
    init:
      mode: never
```

## 5. Запуск приложения

В проекте есть файл `docker-compose.yml`, который поднимает:

- контейнер с **PostgreSQL 17**;
- контейнер с приложением (**Spring Boot**).

Пример `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:17
    container_name: postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: learn_platform
    ports:
      - "5433:5432"   # на хосте порт 5433, внутри контейнера 5432
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - app-network
    healthcheck:
      # проверяем доступность БД с тем же пользователем и БД, что заданы выше
      test: ["CMD-SHELL", "pg_isready -U postgres -d learn_platform"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: learning_service
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/learn_platform
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      # опционально:
      # SPRING_PROFILES_ACTIVE: dev
    ports:
      - "8080:8080"
    networks:
      - app-network

volumes:
  postgres_data:

networks:
  app-network:
    driver: bridge
```

**Шаги запуска**

- Собрать приложение (если образ собирает jar, а не сам Gradle):
`./gradlew clean build`

- Запустить связку app + postgres:
`docker compose up --build`

- Дождаться, пока оба контейнера станут healthy / running.
Проверить можно так: `docker ps`

- Приложение будет доступно по адресу: http://localhost:8080


## 6. Предзаполнение данными

В src/main/resources/data.sql лежит скрипт с демо-данными:
- категории, теги;
- профили и пользователи (преподаватели и студенты);
- курсы с модулями и уроками;
- задания и попытки решения;
- квизы, вопросы, варианты ответов;
- попытки прохождения квиза и выбранные ответы;
- записи на курсы и отзывы.

После первого запуска (с профилем dev, при spring.sql.init.mode=always) база автоматически наполняется демонстрационными данными, и API можно тестировать «из коробки».


## 7. REST API

Ниже приведён обзор основных эндпоинтов.

### 7.1. Курсы, модули, уроки (управление контентом)

**Courses**

- GET /api/courses — список курсов (с фильтрами по категории, тегам и т.п., при необходимости).
- GET /api/courses/{courseId} — детальная информация о курсе.
- POST /api/courses — создать курс (teacherId, categoryId, базовая структура).
- PATCH /api/courses/{courseId} — частично обновить курс (описание, длительность и т.п.).
- DELETE /api/courses/{courseId} — удалить курс (с обработкой каскадов).

**Modules**

- GET /api/courses/{courseId}/modules — список модулей курса.
- POST /api/courses/{courseId}/modules — добавить модуль.
- PATCH /api/modules/{moduleId} — обновить модуль.
- DELETE /api/modules/{moduleId} — удалить модуль.

**Lessons**

- GET /api/modules/{moduleId}/lessons — список уроков в модуле.
- POST /api/modules/{moduleId}/lessons — добавить урок.
- PATCH /api/lessons/{lessonId} — обновить урок.
- DELETE /api/lessons/{lessonId} — удалить урок.

### 7.2. Запись на курс (Enrollment, критерий 4)

- POST /api/courses/{courseId}/enrollments — студент записывается на курс.
- GET /api/courses/{courseId}/enrollments — список студентов, записанных на курс.
- GET /api/students/{studentId}/enrollments — на какие курсы записан конкретный студент.
- DELETE /api/enrollments/{enrollmentId} — отписка от курса (или перевод в статус EXPELLED/COMPLETED, в зависимости от логики).

### 7.3. Задания и решения (Assignment + Submission, критерий 5)

**Assignments**

- POST /api/lessons/{lessonId}/assignments — преподаватель создаёт задание.
- GET /api/lessons/{lessonId}/assignments — список заданий для урока.
- GET /api/assignments/{assignmentId} — детальная информация о задании.

**Submissions**

- POST /api/assignments/{assignmentId}/submissions — студент отправляет решение.
- GET /api/assignments/{assignmentId}/submissions — преподаватель смотрит все решения по заданию.
- GET /api/submissions/{submissionId} — детальная информация о решении.
- PATCH /api/submissions/{submissionId} — выставление оценки, статус ACCEPTED/REJECTED, фидбек.

### 7.4. Тесты/викторины (Quiz, критерий 6)

**Quiz & Questions**

- POST /api/courses/{courseId}/quizzes — создать квиз (MODULE/FINAL, maxScore, duration).
- GET /api/courses/{courseId}/quizzes — список квизов курса.
- GET /api/quizzes/{quizId} — детали квиза, включая вопросы и варианты ответов (через DTO).

**Прохождение теста**

- POST /api/quizzes/{quizId}/submissions — студент начинает/проходит квиз, отправляет выбранные варианты.
- GET /api/quizzes/{quizId}/submissions — преподаватель видит результаты студентов.
- GET /api/students/{studentId}/quiz-submissions — студент видит свои результаты.

## 8. Валидация и обработка ошибок

**Валидация**

Используются javax/jakarta.validation аннотации:
- @NotNull, @NotBlank, @Size, @Email, @Min, @Max и т.п.
- DTO для запросов (CourseCreateDto, AssignmentCreateDto, SubmissionCreateDto, …) помечены @Valid в контроллерах.

Пример
```java
public record CourseCreateDto(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull Integer teacherId,
    @NotNull Integer categoryId,
    @Min(1) @Max(10) Integer yearDuration
) {}
```
**Обработчик ошибок (GlobalExceptionHandler)**

Есть глобальный @RestControllerAdvice, который:

возвращает ответы в формате ProblemDetail (Spring 6+);
отдельные хендлеры для:
- NotFoundException → 404 Not Found;
- MethodArgumentNotValidException → 400 Bad Request с errors: {field: message};
- ConstraintViolationException → 400 для ошибок в @PathVariable/@RequestParam;
- HttpMessageNotReadableException → 400 (кривой JSON);
- DataIntegrityViolationException → 409 Conflict (нарушение уникальных/внешних ключей);
- общий Exception → 500 Internal Server Error.
  
Структура ответа:
```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "Request body validation failed",
  "path": "/api/courses",
  "timestamp": "2025-11-22T11:00:00Z",
  "errors": {
    "title": "must not be blank",
    "categoryId": "must not be null"
  }
}
```

## 9. Репозитории и CRUD

CRUD-операции (create/read/update/delete) покрыты:

- через стандартные методы save, findById, findAll, deleteById и т.д.;
- плюс кастомные методы/спецификации для фильтрации (например, поиск сабмишенов с фильтрами по assignmentId, studentId, status).

## 10. Тестирование (unit + интеграционные)

### 10.1. Интеграционные тесты JPA (@DataJpaTest)

Примеры сценариев:

- CREATE: сохранение курса с модулями и уроками, проверка каскадного persist.
- READ: загрузка курса и проверка связанных сущностей (modules, lessons, teacher, category).
- UPDATE: изменение описания курса или оценки Submission, повторное чтение из БД.
- DELETE: удаление курса и проверка удаления зависимых модулей/уроков (при orphanRemoval = true).

### 10.2. Тест ленивой загрузки (LazyInitializationException)

Отдельный @SpringBootTest, который:

- сохраняет курс с ленивыми модулями;
- грузит курс в транзакции сервиса @Transactional(readOnly = true);
- вне транзакции обращается к course.getModules() и ловит LazyInitializationException — демонстрация того, что связи реально ленивые.

### 10.3. Unit-тесты

Для сервисов:

- тест логики создания курса (проверка, что учитель/категория существуют; модули и уроки правильно привязаны);
- тeст логики записи на курс (проверка уникальности, статуса);
- тест логики приёма/оценки сабмишенов и проведения квизов.

## 11. Примеры сценариев использования
    
### 11.1. Преподаватель

#### 1) Создаёт курс:
- POST /api/courses (title, description, teacherId, categoryId, duration, tags).

#### 2) Добавляет модули и уроки:
- POST /api/courses/{courseId}/modules
- POST /api/modules/{moduleId}/lessons

#### 3) Создаёт задания к урокам:
- POST /api/lessons/{lessonId}/assignments

#### 4) Добавляет квизы:
- POST /api/courses/{courseId}/quizzes

#### 5) Смотрит решения и результаты:
- GET /api/assignments/{assignmentId}/submissions
- GET /api/quizzes/{quizId}/submissions

#### 6) Оценивает решения:
- PATCH /api/submissions/{submissionId} (score, status, feedback).

### 11.2. Студент

#### 1) Смотрит доступные курсы:
- GET /api/courses.

#### 2) Записывается на курс: 
- POST /api/courses/{courseId}/enrollments.

#### 3) Проходит модули и уроки: 
- GET /api/courses/{courseId}/modules
- GET /api/modules/{moduleId}/lessons.

#### 4) Отправляет решения по заданиям: 
- POST /api/assignments/{assignmentId}/submissions.

#### 5) Проходит квизы: 
- POST /api/quizzes/{quizId}/submissions.

#### 6) Смотрит свои результаты и оставляет отзыв:
- GET /api/students/{studentId}/quiz-submissions,
- POST /api/courses/{courseId}/reviews.