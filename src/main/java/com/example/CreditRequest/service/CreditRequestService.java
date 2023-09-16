package com.example.CreditRequest.service;

import com.example.CreditRequest.model.CreditRequest;
import com.example.CreditRequest.repository.ICreditRequestRepository;
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

    public CreditRequest updateCreditRequest(Long id, CreditRequest creditRequest){
        if(creditRequestRepository.existsById(id)){
            creditRequest.setCodRequest(id);
            return creditRequestRepository.save(creditRequest);
        }
        return null;
    }

    public void deleteCreditRequest(Long id){
        creditRequestRepository.deleteById(id);
    }
}
