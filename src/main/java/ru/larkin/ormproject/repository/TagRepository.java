package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}

