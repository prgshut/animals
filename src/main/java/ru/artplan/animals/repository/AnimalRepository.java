package ru.artplan.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.artplan.animals.entity.Animal;
import ru.artplan.animals.entity.User;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

//    @Query("select a from Animal a join fetch a.owner where a.owner = ?1 and a.nikname = ?2")
    Optional<Animal> findByOwnerAndNickName(User userId, String nikName);

    Optional<Animal> findByIdAndOwner(Long animalId, User user);

    List<Animal> findAllByOwner(User user);
}
