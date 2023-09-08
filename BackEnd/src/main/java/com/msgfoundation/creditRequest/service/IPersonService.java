package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.dto.PersonDTO;

public interface IPersonService {
    public PersonDTO save(PersonDTO personDTO);

    public PersonDTO updatePerson(String id, PersonDTO personDTO);
}
