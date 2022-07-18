package ru.artplan.animals.dto;

import lombok.Data;

@Data
public class ResponseError {
    private String message;
    private int code;
}
