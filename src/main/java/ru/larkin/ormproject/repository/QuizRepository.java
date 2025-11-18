package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}

