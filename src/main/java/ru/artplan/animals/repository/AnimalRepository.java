package ru.artplan.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artplan.animals.entity.Animal;
import ru.artplan.animals.entity.User;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByOwnerAndNickName(User userId, String nikName);

    Optional<Animal> findByIdAndOwner(Long animalId, User user);

    List<Animal> findAllByOwner(User user);
}
