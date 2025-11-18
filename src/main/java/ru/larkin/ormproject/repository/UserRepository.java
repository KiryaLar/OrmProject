package ru.larkin.ormproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larkin.ormproject.entity.Role;
import ru.larkin.ormproject.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    Optional<User> findByIdAndRole(Integer id, Role role);
}
