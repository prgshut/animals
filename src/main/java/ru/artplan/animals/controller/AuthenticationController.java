package ru.artplan.animals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.artplan.animals.dto.UserRegistrationDto;
import ru.artplan.animals.entity.User;
import ru.artplan.animals.exceptions.RegistrationError;
import ru.artplan.animals.repository.UserRepository;
import ru.artplan.animals.security.JwtTokenProvider;
import ru.artplan.animals.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final Map<String, List<Date>> userConnectionAttempts = new ConcurrentHashMap<>();
    private static final int NUMBER_LOGIN_ATTEMPTS = 10;
    private static final long TIME_LOGIN_ATTEMPTS_MIN = 60;


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserRegistrationDto request) {
        List<Date> countAttempts = userConnectionAttempts.get(request.getUserName());
        if (countAttempts != null && countAttempts.size() >= NUMBER_LOGIN_ATTEMPTS) {
            var dateFirstConnect = userConnectionAttempts.get(request.getUserName()).get(0);
            long diff = (new Date().getTime() - dateFirstConnect.getTime());
            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (diffMinutes <= TIME_LOGIN_ATTEMPTS_MIN) {
                return new ResponseEntity<>("The number of user authorization attempts has exceeded the allowed number", HttpStatus.FORBIDDEN);
            } else {
                userConnectionAttempts.remove(request.getUserName());
            }
        }
        User user = userService.findByUser(request.getUserName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        try {
            Map<Object, Object> response = login(request);
            userConnectionAttempts.remove(user.getUser());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            if (userConnectionAttempts.containsKey(user.getUser())) {
                userConnectionAttempts.get(user.getUser()).add(new Date());
            } else {
                List<Date> countDate = new ArrayList<>();
                countDate.add(new Date());
                userConnectionAttempts.put(user.getUser(), countDate);
            }
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUserAccount(@RequestBody UserRegistrationDto registrationDto) {
        boolean isUser = userService.findByUser(registrationDto.getUserName()).isPresent();
        if (isUser) {
            return new ResponseEntity<>(new RegistrationError("Username " + registrationDto.getUserName() + " is busy"), HttpStatus.BAD_REQUEST);
        }
        userService.saveUserFromDtoAsEntity(registrationDto);
        Map<Object, Object> response = login(registrationDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/freeLogin")
    public ResponseEntity<?> isFreeUserAccount(@RequestParam(name = "login") String userName) {
        var user = userService.findByUser(userName);
        return ResponseEntity.ok(user.isPresent());
    }

    public Map<Object, Object> login(UserRegistrationDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
        String token = jwtTokenProvider.createToken(userDto.getUserName());
        Map<Object, Object> response = new HashMap<>();
        response.put("user", userDto.getUserName());
        response.put("token", token);
        return response;
    }
}
