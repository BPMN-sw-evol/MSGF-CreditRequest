package com.example.CreditRequest.repository;

import com.example.CreditRequest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonRepository extends JpaRepository<Person, String> {
}
