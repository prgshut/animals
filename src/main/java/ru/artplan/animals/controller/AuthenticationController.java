package ru.artplan.animals.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.artplan.animals.dto.UserRegistrationDto;
import ru.artplan.animals.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserRegistrationDto request) {
        return ResponseEntity.ok(userService.auth(request));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUserAccount(@RequestBody UserRegistrationDto registrationDto) {
        return ResponseEntity.ok(userService.registrationAndLogin(registrationDto));
    }

    @GetMapping("/freeLogin")
    public ResponseEntity<?> isFreeUserAccount(@RequestParam(name = "login") String userName) {
        return ResponseEntity.ok(userService.isFreeUserName(userName));
    }

}
