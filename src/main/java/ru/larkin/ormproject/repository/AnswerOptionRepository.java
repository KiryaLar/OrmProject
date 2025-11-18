package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.AnswerOption;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
}

