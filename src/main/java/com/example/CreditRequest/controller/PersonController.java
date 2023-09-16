package com.example.CreditRequest.controller;

import com.example.CreditRequest.model.Person;
import com.example.CreditRequest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable String id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/create")
    public Person createPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping("/update/{id}")
    public Person updatePerson(@PathVariable String id, @RequestBody Person person) {
        return personService.updatePerson(id, person);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
    }
}
