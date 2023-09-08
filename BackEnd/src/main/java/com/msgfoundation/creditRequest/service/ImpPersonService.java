package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.dto.CoupleDTO;
import com.msgfoundation.creditRequest.dto.PersonDTO;
import com.msgfoundation.creditRequest.model.Person;
import com.msgfoundation.creditRequest.repository.IPersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ImpPersonService implements IPersonService{
    @Autowired
    private IPersonRepository personRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PersonDTO save(PersonDTO personDTO) {
        Person createPerson = modelMapper.map(personDTO, Person.class);
        System.out.println("Person has been created");
        return modelMapper.map(personRepository.save(createPerson), PersonDTO.class);
    }

    @Override
    public PersonDTO updatePerson(String id, PersonDTO personDTO) {
        // Buscar la persona existente por su número de cédula
        Optional<Person> optionalExistingPerson = personRepository.findById(id);

        if (optionalExistingPerson.isPresent()) {
            // La persona existe, actualiza los campos
            Person existingPerson = optionalExistingPerson.get();
            existingPerson.setFullname(personDTO.getFullname());
            existingPerson.setEmail(personDTO.getEmail());
            existingPerson.setGender(personDTO.getGender());
            existingPerson.setBirthDate(personDTO.getBirthDate());
            existingPerson.setPhoneNumber(personDTO.getPhoneNumber());

            // Guardar la persona actualizada en la base de datos
            existingPerson = personRepository.save(existingPerson);

            // Convierte la entidad actualizada a un DTO y devuelve el DTO
            PersonDTO updatedPersonDTO = modelMapper.map(existingPerson, PersonDTO.class);
            return updatedPersonDTO;
        } else {
            // La persona no existe, manejar este caso (por ejemplo, lanzar una excepción)
            throw new EntityNotFoundException("Persona no encontrada con el ID: " + id);
        }
    }

    private PersonDTO findById(String id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            return modelMapper.map(person, PersonDTO.class);
        } else {
            System.out.println("La persona no existe");
            return null; // Devuelve null u otra indicación de que la persona no se encontró
        }
    }


}
