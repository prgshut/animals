package ru.artplan.animals.exceptions;

import lombok.Data;

@Data
public class AnimalError extends RuntimeException {
    private String messages;

    public AnimalError(String message) {
        super(message);
        this.messages = message;
    }


}
