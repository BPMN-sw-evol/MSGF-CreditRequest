package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CoupleDTO;
import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final CoupleController coupleController;

    @Autowired
    public PersonController(PersonService personService, CoupleController coupleController) {
        this.personService = personService;
        this.coupleController = coupleController;
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
    public String createPerson(@RequestBody CreditInfoDTO creditInfoDTO) {
        List<Person> people = creditInfoDTO.getPeople();

        // Si todas las validaciones son exitosas, proceder con la creación de personas y pareja
        personService.createPerson(people.get(0));
        personService.createPerson(people.get(1));

        CoupleDTO couple = new CoupleDTO();
        couple.setPartner1Id(people.get(0).getId());
        couple.setPartner2Id(people.get(1).getId());
        coupleController.createCouple(couple);

        return "People has been created successfully";
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
