package ru.artplan.animals.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.artplan.animals.dto.UserRegistrationDto;
import ru.artplan.animals.entity.Status;
import ru.artplan.animals.entity.User;
import ru.artplan.animals.exceptions.ConnectException;
import ru.artplan.animals.exceptions.RegistrationError;
import ru.artplan.animals.repository.UserRepository;
import ru.artplan.animals.security.JwtTokenProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    private final Map<String, List<Date>> userConnectionAttempts = new ConcurrentHashMap<>();
    private static final int NUMBER_LOGIN_ATTEMPTS = 10;
    private static final long TIME_LOGIN_ATTEMPTS_MIN = 60;

    public Map<Object, Object> registrationAndLogin(UserRegistrationDto registrationDto) {
        boolean isUser = findByUser(registrationDto.getUserName()).isPresent();
        if (isUser) {
            throw new RegistrationError("Username " + registrationDto.getUserName() + " is busy");
        }
        saveUserFromDtoAsEntity(registrationDto);
        return login(registrationDto);
    }

    public void saveUserFromDtoAsEntity(UserRegistrationDto userDto) {
        User user = new User();
        user.setUser(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }

    public User findUser(String user) {
        return findByUser(user).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }


    public boolean isFreeUserName(String userName) {
        return findByUser(userName).isPresent();
    }

    public Map<Object, Object> auth(UserRegistrationDto request) {
        List<Date> countAttempts = userConnectionAttempts.get(request.getUserName());
        if (countAttempts != null && countAttempts.size() >= NUMBER_LOGIN_ATTEMPTS) {
            var dateFirstConnect = userConnectionAttempts.get(request.getUserName()).get(0);
            long diff = (new Date().getTime() - dateFirstConnect.getTime());
            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (diffMinutes <= TIME_LOGIN_ATTEMPTS_MIN) {
                throw new ConnectException("The number of user authorization attempts has exceeded the allowed number");
            } else {
                userConnectionAttempts.remove(request.getUserName());
            }
        }
        User user = findUser(request.getUserName());
        try {
            Map<Object, Object> response = login(request);
            userConnectionAttempts.remove(user.getUser());
            return response;
        } catch (AuthenticationException e) {
            if (userConnectionAttempts.containsKey(user.getUser())) {
                userConnectionAttempts.get(user.getUser()).add(new Date());
            } else {
                List<Date> countDate = new ArrayList<>();
                countDate.add(new Date());
                userConnectionAttempts.put(user.getUser(), countDate);
            }
            throw new ConnectException("Invalid email/password combination");
        }
    }

    private Optional<User> findByUser(String userName) {
        return userRepository.findByUser(userName);
    }

    private Map<Object, Object> login(UserRegistrationDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
        String token = jwtTokenProvider.createToken(userDto.getUserName());
        Map<Object, Object> response = new HashMap<>();
        response.put("user", userDto.getUserName());
        response.put("token", token);
        return response;
    }
}
