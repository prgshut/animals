package ru.artplan.animals.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artplan.animals.dto.AnimalDto;
import ru.artplan.animals.entity.Animal;
import ru.artplan.animals.entity.Kind;
import ru.artplan.animals.entity.User;
import ru.artplan.animals.exceptions.AnimalError;
import ru.artplan.animals.repository.AnimalRepository;
import ru.artplan.animals.repository.KindRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnimalService {
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private KindRepository kindRepository;
    @Autowired
    private UserService userService;

    public void addAnimal(AnimalDto dto, String userName) throws AnimalError {
        User user = userService.findUser(userName);

        Kind kindDb = kindRepository.findByName(dto.getKind()).orElseThrow(() -> new AnimalError("The type of animal is unknown, it was not possible to add a pet"));
        var nik = animalRepository.findByOwnerAndNickName(user, dto.getNickName());
        if (nik.isPresent()) {
            throw new AnimalError("Animal name already taken");
        }
        Animal animal = new Animal();
        animal.setBirthday(dto.getBirthday());
        animal.setGender(dto.getGender());
        animal.setKind(kindDb);
        animal.setNickName(dto.getNickName());
        animal.setOwner(user);
        animalRepository.save(animal);
    }

    public AnimalDto getAnimalToDto(String userName, Long animalId) {
        User user = userService.findUser(userName);
        Animal animal = animalRepository.findByIdAndOwner(animalId, user).orElseThrow(() -> new AnimalError("There is no pet with this number"));

        return toDto(animal);
    }

    public List<AnimalDto> getAllAnimalToDto(String userName) {
        User user = userService.findUser(userName);
        List<Animal> animals = animalRepository.findAllByOwner(user);
        List<AnimalDto> animalsDto = new ArrayList<>();
        for (Animal animal : animals) {
            animalsDto.add(toDto(animal));
        }
        return animalsDto;
    }

    private AnimalDto toDto(Animal animal) {
        AnimalDto dto = new AnimalDto();
        dto.setBirthday(animal.getBirthday());
        dto.setGender(animal.getGender());
        dto.setKind(animal.getKind().getName());
        dto.setNickName(animal.getNickName());
        return dto;
    }
}
