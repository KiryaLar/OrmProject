-- ==== CATEGORY ====
INSERT INTO category (id, name)
VALUES (1, 'Programming'),
       (2, 'Mathematics');

-- ==== TAG ====
INSERT INTO tag (id, name)
VALUES (1, 'Java'),
       (2, 'Spring'),
       (3, 'Beginner'),
       (4, 'Algorithms');

-- ==== PROFILE ====
INSERT INTO profile (id, birth_date, gender, created_at, updated_at, bio, avatar_url, city, phone_number)
VALUES (1, '1985-03-10', 0, '2025-01-01 10:00:00+00', '2025-01-01 10:00:00+00',
        'Senior Java developer and instructor', NULL, 'Moscow', '+7-900-111-11-11'),
       (2, '1990-07-20', 1, '2025-01-01 10:05:00+00', '2025-01-01 10:05:00+00',
        'Algorithms and data structures teacher', NULL, 'Saint Petersburg', '+7-900-222-22-22'),
       (3, '2003-05-15', 0, '2025-01-01 10:10:00+00', '2025-01-01 10:10:00+00',
        'First-year CS student', NULL, 'Kazan', '+7-900-333-33-33'),
       (4, '2002-11-02', 1, '2025-01-01 10:15:00+00', '2025-01-01 10:15:00+00',
        'Interested in backend development', NULL, 'Novosibirsk', '+7-900-444-44-44'),
       (5, '2001-09-09', 0, '2025-01-01 10:20:00+00', '2025-01-01 10:20:00+00',
        'Math and algorithms enthusiast', NULL, 'Yekaterinburg', '+7-900-555-55-55');

-- ==== USERS (teachers + students) ====
INSERT INTO users (id, profile_id, email, name, role)
VALUES (1, 1, 'teacher1@example.com', 'Ivan Petrov', 'TEACHER'),
       (2, 2, 'teacher2@example.com', 'Anna Smirnova', 'TEACHER'),
       (3, 3, 'student1@example.com', 'Sergey Ivanov', 'STUDENT'),
       (4, 4, 'student2@example.com', 'Olga Sidorova', 'STUDENT'),
       (5, 5, 'student3@example.com', 'Dmitry Volkov', 'STUDENT');

-- ==== COURSE ====
INSERT INTO course (id, category_id, teacher_id, year_duration, uuid, description, title)
VALUES (1, 1, 1, 1,
        '11111111-1111-1111-1111-111111111111',
        'Foundational course on Java programming for absolute beginners.',
        'Java Basics'),
       (2, 2, 2, 1,
        '22222222-2222-2222-2222-222222222222',
        'Introductory course on algorithms and complexity analysis.',
        'Algorithms 101');

-- ==== COURSE_TAG ====
INSERT INTO course_tag (course_id, tag_id)
VALUES (1, 1), -- Java Basics -> Java
       (1, 2), -- Java Basics -> Spring
       (1, 3), -- Java Basics -> Beginner
       (2, 4), -- Algorithms 101 -> Algorithms
       (2, 3); -- Algorithms 101 -> Beginner

-- ==== ENROLLMENT (students enrolled in courses) ====
INSERT INTO enrollment (id, course_id, enrolled_at, student_id, status)
VALUES (1, 1, '2025-01-10', 3, 'ACTIVE'),
       (2, 1, '2025-01-10', 4, 'COMPLETED'),
       (3, 2, '2025-01-12', 5, 'ACTIVE');

-- ==== MODULES ====
INSERT INTO module (id, course_id, module_order, title)
VALUES (1, 1, 1, 'Getting Started with Java'),
       (2, 1, 2, 'OOP Basics'),
       (3, 2, 1, 'Introduction to Algorithms');

-- ==== LESSONS ====
INSERT INTO lesson (id, module_id, lesson_order, title, description, video_url)
VALUES (1, 1, 1, 'What is Java?',
        'Overview of Java platform, JVM and JDK.',
        'https://example.com/v/java-intro'),
       (2, 1, 2, 'Setting up the Development Environment',
        'Installing JDK and IntelliJ IDEA.',
        'https://example.com/v/java-setup'),
       (3, 2, 1, 'Classes and Objects',
        'Basic OOP concepts in Java.',
        'https://example.com/v/oop-classes'),
       (4, 2, 2, 'Encapsulation and Access Modifiers',
        'Encapsulation, getters/setters, access levels.',
        'https://example.com/v/oop-encapsulation'),
       (5, 3, 1, 'Big-O Notation',
        'Complexity analysis and Big-O basics.',
        'https://example.com/v/alg-big-o');

-- ==== MODULE_LESSONS (связка module <-> lessons) ====
INSERT INTO module_lessons (lessons_id, module_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 3);

-- ==== ASSIGNMENT (homework for lessons) ====
INSERT INTO assignment (id, lesson_id, max_score, due_date, title, description)
VALUES (1, 1, 100, '2025-02-01 23:59:00+00',
        'Intro Java Homework',
        'Write a short console program that prints your name.'),
       (2, 3, 100, '2025-02-05 23:59:00+00',
        'OOP Classes Task',
        'Implement a simple class hierarchy with inheritance.'),
       (3, 5, 100, '2025-02-07 23:59:00+00',
        'Big-O Exercises',
        'Determine time complexity for several algorithms.');

-- ==== QUIZ (module and final quizzes) ====
INSERT INTO quiz (id, course_id, module_id, duration_in_minutes, max_score, start_date, quiz_type, title)
VALUES (1, 1, 2, 30, 100, '2025-02-10 10:00:00+00', 'MODULE', 'OOP Basics Quiz'),
       (2, 1, NULL, 60, 100, '2025-03-01 09:00:00+00', 'FINAL', 'Java Basics Final Exam'),
       (3, 2, 3, 30, 100, '2025-02-15 11:00:00+00', 'MODULE', 'Algorithms Intro Quiz');

-- ==== QUESTION ====
INSERT INTO question (id, quiz_id, max_score, text, type)
VALUES (1, 1, 5, 'What does JVM stand for?', 'SINGLE_CHOICE'),
       (2, 1, 10, 'Select all correct features of Java.', 'MULTIPLE_CHOICE'),
       (3, 3, 5, 'What is Big-O used for?', 'SINGLE_CHOICE'),
       (4, 2, 10, 'Which of these are valid Java access modifiers?', 'MULTIPLE_CHOICE');

-- ==== ANSWER_OPTION ====
INSERT INTO answer_option (id, question_id, text, is_correct)
VALUES
    -- Q1: What does JVM stand for?
    (1, 1, 'Java Virtual Machine', TRUE),
    (2, 1, 'Java Variable Manager', FALSE),

    -- Q2: Select all correct features of Java.
    (3, 2, 'Garbage collection', TRUE),
    (4, 2, 'Multiple inheritance of classes', FALSE),
    (5, 2, 'Platform independence', TRUE),

    -- Q3: What is Big-O used for?
    (6, 3, 'To describe algorithm complexity', TRUE),
    (7, 3, 'To specify memory layout of objects', FALSE);

-- ==== COURSE_REVIEW ====
INSERT INTO course_review (id, course_id, student_id, rating, created_at, comment)
VALUES (1, 1, 3, 5, '2025-02-20 18:00:00+00',
        'Great introduction to Java, clear explanations.'),
       (2, 1, 4, 4, '2025-02-21 19:30:00+00',
        'Good course, but I would like more practice tasks.');

-- ==== QUIZ_SUBMISSION (student attempts) ====
INSERT INTO quiz_submission (id, quiz_id, student_id, total_score, submit_time)
VALUES (1, 1, 3, 12, '2025-02-11 12:00:00+00');

-- ==== QUESTION_SUBMISSION ====
INSERT INTO question_submission (id, quiz_submission_id, question_id, score)
VALUES (1, 1, 1, 5), -- student1 answered Q1 correctly
       (2, 1, 2, 7);
-- partial score on Q2

-- ==== SELECTED_ANSWER_OPTION ====
INSERT INTO selected_answer_option (id, question_submission_id, answer_option_id, uid)
VALUES
    -- For Q1: chose correct option
    (1, 1, 1, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa0001'),

    -- For Q2: chose options 3 and 5 (correct ones)
    (2, 2, 3, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa0002'),
    (3, 2, 5, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa0003');

-- ==== SUBMISSION (assignment submissions by students) ====
INSERT INTO submission (id, assignment_id, user_id, score, submitted_at, content, feedback, status)
VALUES (1, 1, 3, 95, '2025-01-25 20:00:00+00',
        'Link to GitHub repo with my intro Java program.',
        'Excellent work, clean code.', 'ACCEPTED'),
       (2, 1, 4, 70, '2025-01-26 21:30:00+00',
        'Console app with basic output.',
        'Works, but lacks comments and proper naming.', 'ACCEPTED'),
       (3, 3, 5, NULL, NULL,
        'Will submit soon, still working on Big-O tasks.',
        NULL, 'PENDING');