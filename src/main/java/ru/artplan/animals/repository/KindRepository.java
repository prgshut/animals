package ru.artplan.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artplan.animals.entity.Kind;

import java.util.Optional;

public interface KindRepository extends JpaRepository<Kind, Long> {

    Optional<Kind> findByName(String kind);
}
