package ru.artplan.animals.exceptions;

import lombok.Data;

@Data
public class RegistrationError extends RuntimeException{
    private String messages;

    public RegistrationError(String message) {
        super(message);
        this.messages = message;
    }


}
