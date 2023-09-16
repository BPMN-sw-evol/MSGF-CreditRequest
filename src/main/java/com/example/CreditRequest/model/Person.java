package com.example.CreditRequest.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON")
@Data
public class Person {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthDate")
    private String birthDate;
    @Column(name = "phoneNumber")
    private String phoneNumber;
}
