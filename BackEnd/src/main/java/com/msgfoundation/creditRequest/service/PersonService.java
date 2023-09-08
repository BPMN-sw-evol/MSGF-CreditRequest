package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.model.Person;
import com.msgfoundation.creditRequest.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final IPersonRepository personRepository;

    @Autowired
    public PersonService(IPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(String id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(String id, Person person) {
        if (personRepository.existsById(id)) {
            person.setId(id);
            return personRepository.save(person);
        }
        return null; // Handle not found scenario
    }

    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }
}
