package com.MSGFoundation.dto;

import com.MSGFoundation.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class PersonListDTO {
    private List<Person> people;
}
