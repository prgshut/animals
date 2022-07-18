package ru.artplan.animals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.artplan.animals.dto.AnimalDto;
import ru.artplan.animals.services.AnimalService;
import ru.artplan.animals.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {
    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addAnimal(@RequestBody AnimalDto dto, Principal principal) {
        animalService.addAnimal(dto, principal.getName());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimal(@PathVariable(value = "id") String animalId, Principal principal) {
        Long id = Long.parseLong(animalId);
        return ResponseEntity.ok(animalService.getAnimalToDto(principal.getName(), id));
    }

    @GetMapping
    public ResponseEntity<List<AnimalDto>> getAllAnimal(Principal principal) {
        return ResponseEntity.ok(animalService.getAllAnimalToDto(principal.getName()));
    }

}
