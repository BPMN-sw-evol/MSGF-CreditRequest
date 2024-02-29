package com.MSGFoundation.service;

import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.repository.ICreditRequestRepository;
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

    public void createCreditRequest(CreditRequest creditRequest){
        creditRequestRepository.save(creditRequest);
    }

    public void updateCreditRequest(Long id, CreditRequest creditRequest){
        if(creditRequestRepository.existsById(id)){
            creditRequest.setCodRequest(id);
            creditRequestRepository.save(creditRequest);
        }
    }

    public void deleteCreditRequest(Long id){
        creditRequestRepository.deleteById(id);
    }

    public List<CreditRequest> findCreditByCouple(Couple couple) {
        return creditRequestRepository.findByApplicantCouple(couple);
    }

    public CreditRequest getCreditRequestByProcessId(String processId) {
        return creditRequestRepository.findByProcessId(processId);
    }

    public CreditRequest findFirstByOrderByRequestDateDesc() {
        return creditRequestRepository.findFirstByOrderByRequestDateDesc();
    }
}
