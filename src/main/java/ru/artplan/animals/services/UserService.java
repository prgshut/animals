package ru.artplan.animals.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.artplan.animals.dto.UserRegistrationDto;
import ru.artplan.animals.entity.Status;
import ru.artplan.animals.entity.User;
import ru.artplan.animals.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUserFromDtoAsEntity(UserRegistrationDto userDto){
        User user = new User();
        user.setUser(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }

    public Optional<User> findByUser(String user){
        return userRepository.findByUser(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
