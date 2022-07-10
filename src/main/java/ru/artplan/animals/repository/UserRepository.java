package ru.artplan.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artplan.animals.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUser(String user);
}
