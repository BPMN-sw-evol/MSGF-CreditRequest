package com.msgfoundation.creditRequest.controller;

import com.msgfoundation.creditRequest.dto.PersonDTO;
import com.msgfoundation.creditRequest.service.IPersonService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private IPersonService personService;

    @PostMapping("/create")
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO){
        PersonDTO createdPerson = personService.save(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable String id, @RequestBody PersonDTO personDTO){
        PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(personDTO);
    }
}
