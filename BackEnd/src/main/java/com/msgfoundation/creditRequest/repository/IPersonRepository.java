package com.msgfoundation.creditRequest.repository;

import com.msgfoundation.creditRequest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonRepository extends JpaRepository<Person, String> {
}
