package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}

