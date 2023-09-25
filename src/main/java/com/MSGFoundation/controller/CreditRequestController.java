package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.CreditRequestService;
import com.MSGFoundation.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private final CreditRequestService creditRequestService;
    private final CoupleController coupleController;
    private final PersonController personController;
    private final ProcessController processController;

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, CoupleController coupleController, PersonController personController, ProcessController processController){
        this.creditRequestService = creditRequestService;
        this.coupleController = coupleController;
        this.personController = personController;
        this.processController = processController;
    }

    @GetMapping("/{coupleId}")
    public ResponseEntity<List<CreditRequest>> findCreditRequestByCouple(@PathVariable Long coupleId){
        try{
            Couple couple = coupleController.getCoupleById(coupleId);
            List<CreditRequest> creditRequest = creditRequestService.findCreditByCouple(couple);
            return ResponseEntity.ok(creditRequest);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/create")
    public RedirectView createCreditRequest(@ModelAttribute CreditInfoDTO creditInfoDTO, RedirectAttributes redirectAttributes) {
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
        creditRequest.setStatus(RequestStatus.DRAFT.toString());
        LocalDateTime currentDate = LocalDateTime.now();
        creditRequest.setRequestDate(currentDate);

        Long coupleId = coupleController.getCouplebyIds(partner1.getId(), partner2.getId()).getBody();
        Couple couple = coupleController.getCoupleById(coupleId);
        creditRequest.setApplicantCouple(couple);
        creditInfoDTO.setApplicantCoupleId(coupleId);
        redirectAttributes.addAttribute("coupleId",coupleId);

        creditRequestService.createCreditRequest(creditRequest);
        String processId = processController.startProcessInstance(creditInfoDTO);
        List<CreditRequest> updateCredit = creditRequestService.findCreditByCouple(couple);
        for (CreditRequest request : updateCredit) {
            if ("DRAFT".equals(request.getStatus())) {
                System.out.println("Solicitud en estado draft: " + request.getCodRequest());
                request.setProcessId(processId);
                creditRequestService.updateCreditRequest(request.getCodRequest(), request);
            }
        }

        return new RedirectView("/view-credit");
    }

    @PostMapping("/update")
    public CreditRequest updateCreditRequest(@ModelAttribute("creditInfoDTO") CreditInfoDTO creditInfoDTO){

        System.out.println("esto es el id: "+creditInfoDTO.getApplicantCoupleId());
        List<Person> people = creditInfoDTO.getPeople();
        System.out.println(people.get(0).toString());
        personController.updatePerson(people.get(0).getId(),people.get(0));
        personController.updatePerson(people.get(1).getId(),people.get(1));
        Long id = 1L;
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setMarriageYears(creditInfoDTO.getMarriageYears());
        creditRequest.setBothEmployees(creditInfoDTO.getBothEmployees());
        creditRequest.setHousePrices(creditInfoDTO.getHousePrices());
        creditRequest.setQuotaValue(creditInfoDTO.getQuotaValue());
        creditRequest.setCoupleSavings(creditInfoDTO.getCoupleSavings());

        return creditRequestService.updateCreditRequest(id, creditRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCreditRequest(@PathVariable Long id){
        creditRequestService.deleteCreditRequest(id);
    }

    @GetMapping("/findbyid/{processId}")
    public CreditRequest getCreditRequestByProcessId(@PathVariable String processId){
        return creditRequestService.getCreditRequestByProcessId(processId);
    }
}
