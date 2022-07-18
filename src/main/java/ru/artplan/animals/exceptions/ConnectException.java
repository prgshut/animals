package ru.artplan.animals.exceptions;

import lombok.Data;

@Data
public class ConnectException extends RuntimeException{
    private String message;

    public ConnectException(String message) {
        super(message);
        this.message = message;
    }
}
