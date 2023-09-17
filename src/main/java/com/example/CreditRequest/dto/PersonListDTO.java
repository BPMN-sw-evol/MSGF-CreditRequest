package com.example.CreditRequest.dto;

import com.example.CreditRequest.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class PersonListDTO {
    private List<Person> people;
}
