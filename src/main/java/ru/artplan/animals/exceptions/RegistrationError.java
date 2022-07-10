package ru.artplan.animals.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class RegistrationError {
    private int status;
    private String messages;
    private Date timestamp;

    public RegistrationError(String message) {
        this.status = HttpStatus.BAD_REQUEST.value();
        this.messages = message;
        this.timestamp = new Date();
    }


}
