package com.msgfoundation.creditRequest.dto;

import lombok.Data;

import javax.persistence.Column;
@Data
public class PersonDTO {
    private String id;
    private String fullname;
    private String email;
    private String gender;
    private String birthDate;
    private String phoneNumber;
}
