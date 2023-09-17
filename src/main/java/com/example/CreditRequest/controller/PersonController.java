package com.example.CreditRequest.controller;

import com.example.CreditRequest.dto.CoupleDTO;
import com.example.CreditRequest.dto.PersonListDTO;
import com.example.CreditRequest.model.Person;
import com.example.CreditRequest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
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
    public String createPerson(@ModelAttribute PersonListDTO personListDTO, RedirectAttributes redirect) {
        List<Person> people = personListDTO.getPeople();
        for( Person person: people){
            personService.createPerson(person);
        }
        CoupleDTO couple = new CoupleDTO();
        couple.setPartner1Id(people.get(0).getId());
        couple.setPartner2Id(people.get(1).getId());
        coupleController.createCouple(couple);
        redirect.addFlashAttribute("msgSuccessfully","The people has been added successfully");
        return "redirect/";
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
