package com.example.CreditRequest.controller;

import com.example.CreditRequest.dto.CreditRequestDTO;
import com.example.CreditRequest.model.Couple;
import com.example.CreditRequest.model.CreditRequest;
import com.example.CreditRequest.service.CoupleService;
import com.example.CreditRequest.service.CreditRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private CreditRequestService creditRequestService;
    private final CoupleService coupleService;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, CoupleService coupleService){
        this.creditRequestService = creditRequestService;
        this.coupleService = coupleService;
    }

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @PostMapping("/create")
    public ResponseEntity<CreditRequest> createCreditRequest(@RequestBody CreditRequestDTO creditRequestDTO) {
        try {
            CreditRequest creditRequest = new CreditRequest();
            creditRequest.setMarriageYears(creditRequestDTO.getMarriageYears());
            creditRequest.setBothEmployees(creditRequestDTO.getBothEmployees());
            creditRequest.setHousePrices(creditRequestDTO.getHousePrices());
            creditRequest.setQuotaValue(creditRequestDTO.getQuotaValue());
            creditRequest.setCoupleSavings(creditRequestDTO.getCoupleSavings());
            creditRequest.setFinancialViability(creditRequestDTO.getFinancialViability());
            creditRequest.setIsValid(creditRequestDTO.getIsValid());

            Long coupleId = creditRequestDTO.getApplicantCouple(); // Obtener el ID de la pareja del DTO
            Couple applicantCouple = coupleService.getCoupleById(coupleId);
            creditRequest.setApplicantCouple(applicantCouple);
            CreditRequest createdCreditRequest = creditRequestService.createCreditRequest(creditRequest);

            return new ResponseEntity<>(createdCreditRequest, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            // Manejar el caso en el que la pareja no se encuentra
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
