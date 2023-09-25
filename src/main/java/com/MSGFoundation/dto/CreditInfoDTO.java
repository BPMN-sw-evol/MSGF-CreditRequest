package com.MSGFoundation.dto;

import com.MSGFoundation.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class CreditInfoDTO {
    private List<Person> people;
    private Long applicantCoupleId;
    private String processId;
    private Long marriageYears;
    private Boolean bothEmployees;
    private Long housePrices;
    private Long quotaValue;
    private Long coupleSavings;
    private String status;
}
