package ru.artplan.animals.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.artplan.animals.dto.ResponseError;
import ru.artplan.animals.exceptions.AnimalError;
import ru.artplan.animals.exceptions.ConnectException;
import ru.artplan.animals.exceptions.JwtAuthenticationException;
import ru.artplan.animals.exceptions.RegistrationError;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(value = AnimalError.class)
    public ResponseEntity<ResponseError> sendAnimalException(AnimalError e) {
        ResponseError response = createResponse(e, 300);
        return new ResponseEntity<>(response, HttpStatus.MULTIPLE_CHOICES);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ResponseError> sendUserNotFound(UsernameNotFoundException e) {
        ResponseError response = createResponse(e, 400);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = RegistrationError.class)
    public ResponseEntity<ResponseError> sendRegistrationError(RegistrationError e) {
        ResponseError response = createResponse(e, 404);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = JwtAuthenticationException.class)
    public ResponseEntity<ResponseError> sendAuthError(JwtAuthenticationException e) {
        ResponseError response = createResponse(e, 450);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(value = ConnectException.class)
    public ResponseEntity<ResponseError> maxConnectError(ConnectException e) {
        ResponseError response = createResponse(e, 455);
        return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
    }

    private ResponseError createResponse(Throwable e, int code) {
        ResponseError response = new ResponseError();
        response.setCode(code);
        response.setMessage(e.getMessage());
        return response;
    }
}
