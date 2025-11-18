package ru.larkin.ormproject.exception;

public class NotFoundException extends RuntimeException {
    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException forTeacher(Integer userId) {
        String message = String.format("Teacher with identifier %d not found.", userId);
        return new NotFoundException(message);
    }

    public static NotFoundException forCategory(Integer categoryId) {
        String message = String.format("Category with identifier %d not found.", categoryId);
        return new NotFoundException(message);
    }

    public static NotFoundException forCourse(Integer courseId) {
        String message = String.format("Course with identifier %d not found.", courseId);
        return new NotFoundException(message);
    }

    public static NotFoundException forModule(Long moduleId) {
        String message = String.format("Module with identifier %d not found.", moduleId);
        return new NotFoundException(message);
    }

    public static NotFoundException forLesson(Long lessonId) {
        String message = String.format("Lesson with identifier %d not found.", lessonId);
        return new NotFoundException(message);
    }

    public static NotFoundException forUser(Integer userId) {
        String message = String.format("User with identifier %d not found.", userId);
        return new NotFoundException(message);
    }

    public static NotFoundException forEnrollment(Integer studentId, Integer courseId) {
        String message = String.format("Enrollment for student with identifier %d in course with identifier %d not found.", studentId, courseId);
        return new NotFoundException(message);
    }

    public static RuntimeException forQuiz(Long quizId) {
        String message = String.format("Quiz with identifier %d not found.", quizId);
        return new NotFoundException(message);
    }

    public static RuntimeException forQuestion(Long questionId) {
        String message = String.format("Question with identifier %d not found.", questionId);
        return new NotFoundException(message);
    }

    public static RuntimeException forAnswerOption(Long answerOptionId) {
        String message = String.format("Answer option with identifier %d not found.", answerOptionId);
        return new NotFoundException(message);
    }
}
