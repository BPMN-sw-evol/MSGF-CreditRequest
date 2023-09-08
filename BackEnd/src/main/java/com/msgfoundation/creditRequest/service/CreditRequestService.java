package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.model.CreditRequest;
import com.msgfoundation.creditRequest.repository.ICreditRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditRequestService {
    private final ICreditRequestRepository creditRequestRepository;
    @Autowired
    public CreditRequestService(ICreditRequestRepository creditRequestRepository){
        this.creditRequestRepository = creditRequestRepository;
    }

    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestRepository.findAll();
    }

    public CreditRequest createCreditRequest(CreditRequest creditRequest){
        return creditRequestRepository.save(creditRequest);
    }
}
