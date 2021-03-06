package ru.artplan.animals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDto {
    private String kind;
    private Date birthday;
    private char gender;
    private String nickName;
}
