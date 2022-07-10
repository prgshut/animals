package ru.artplan.animals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.artplan.animals.dto.AnimalDto;
import ru.artplan.animals.entity.User;
import ru.artplan.animals.exceptions.AnimalError;
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
    public ResponseEntity<?> addAnimal(@RequestBody AnimalDto dto, Principal principal){
        try {
            User user = userService.findByUser(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            animalService.addAnimal(dto, user);
            return  ResponseEntity.ok(HttpStatus.CREATED);
        }catch (AnimalError e){
            return new ResponseEntity<>(new AnimalError("he type of animal is unknown, it was not possible to add a pet"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimal(@PathVariable (value = "id") String animalId, Principal principal){
        Long id = Long.parseLong(animalId);
        User user = userService.findByUser(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return ResponseEntity.ok(animalService.getAnimalToDto(user, id));
    }

    @GetMapping
    public ResponseEntity<List<AnimalDto>> getAllAnimal(Principal principal) {
        User user = userService.findByUser(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return ResponseEntity.ok(animalService.getAllAnimalToDto(user));
    }

}
