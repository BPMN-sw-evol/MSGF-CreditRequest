package com.MSGFoundation.dto;

import com.MSGFoundation.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class CreditInfoDTO {
    private List<Person> people;
    private Long applicantCoupleId;
    private Long marriageYears;
    private Boolean bothEmployees;
    private Float housePrices;
    private Float quotaValue;
    private Float coupleSavings;
    private String status;
}
