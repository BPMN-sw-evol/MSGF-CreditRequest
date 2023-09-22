package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.CoupleService;
import com.MSGFoundation.service.CreditRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private final CreditRequestService creditRequestService;
    private final CoupleService coupleService;

    private final PersonController personController;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, CoupleService coupleService, PersonController personController){
        this.creditRequestService = creditRequestService;
        this.coupleService = coupleService;
        this.personController = personController;
    }

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCreditRequest(@ModelAttribute CreditInfoDTO creditInfoDTO) {
        List<Person> people = creditInfoDTO.getPeople();
        Person partner1 = people.get(0);
        Person partner2 = people.get(1);

        personController.createPerson(creditInfoDTO);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setMarriageYears(creditInfoDTO.getMarriageYears());
        creditRequest.setBothEmployees(creditInfoDTO.getBothEmployees());
        creditRequest.setHousePrices(creditInfoDTO.getHousePrices());
        creditRequest.setQuotaValue(creditInfoDTO.getQuotaValue());
        creditRequest.setCoupleSavings(creditInfoDTO.getCoupleSavings());
        creditRequest.setFinancialViability(false);
        creditRequest.setIsValid(false);

        Long coupleId = coupleService.getCouplebyIds(partner1.getId(), partner2.getId());
        Couple couple = coupleService.getCoupleById(coupleId);
        creditRequest.setApplicantCouple(couple);

        creditRequestService.createCreditRequest(creditRequest);

        return ResponseEntity.ok("credit request created");
    }

    @PutMapping("/update/{id}")
    public CreditRequest updateCreditRequest(@PathVariable Long id, @RequestBody CreditRequest creditRequest){
        return creditRequestService.updateCreditRequest(id, creditRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCreditRequest(@PathVariable Long id){
        creditRequestService.deleteCreditRequest(id);
    }

}
