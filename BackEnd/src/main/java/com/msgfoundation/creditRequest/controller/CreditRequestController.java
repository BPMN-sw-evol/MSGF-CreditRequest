package com.msgfoundation.creditRequest.controller;

import com.msgfoundation.creditRequest.model.CreditRequest;
import com.msgfoundation.creditRequest.service.CreditRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private CreditRequestService creditRequestService;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService){
        this.creditRequestService = creditRequestService;
    }

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @PostMapping("/create")
    public CreditRequest createCreditRequest(@RequestBody CreditRequest creditRequest){
        return creditRequestService.createCreditRequest(creditRequest);
    }

}
