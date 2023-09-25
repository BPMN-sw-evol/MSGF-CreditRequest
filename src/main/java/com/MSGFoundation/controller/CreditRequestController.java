package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.CoupleService;
import com.MSGFoundation.service.CreditRequestService;
import com.MSGFoundation.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpHeaders;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private final CreditRequestService creditRequestService;
    private final CoupleService coupleService;
    private final PersonController personController;
    private final RestTemplate restTemplate;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, CoupleService coupleService, PersonController personController, RestTemplate restTemplate){
        this.creditRequestService = creditRequestService;
        this.coupleService = coupleService;
        this.personController = personController;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @GetMapping("/{coupleId}")
    public ResponseEntity<List<CreditRequest>> findCreditRequestByCouple(@PathVariable Long coupleId){
        try{
            Couple couple = coupleService.getCoupleById(coupleId);
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

        Long coupleId = coupleService.getCouplebyIds(partner1.getId(), partner2.getId());
        Couple couple = coupleService.getCoupleById(coupleId);
        creditRequest.setApplicantCouple(couple);
        redirectAttributes.addAttribute("coupleId",coupleId);

        creditRequestService.createCreditRequest(creditRequest);

        // Construir el cuerpo de la solicitud para Camunda
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear un mapa para los atributos que deseas enviar
        Map<String, Object> variables = new HashMap<>();
        variables.put("marriageYears", Map.of("value", creditInfoDTO.getMarriageYears(), "type", "Long"));
        variables.put("bothEmployees", Map.of("value", creditInfoDTO.getBothEmployees(), "type", "Boolean"));
        variables.put("housePrices", Map.of("value", creditInfoDTO.getHousePrices(), "type", "Long"));
        variables.put("quotaValue", Map.of("value", creditInfoDTO.getQuotaValue(), "type", "Long"));
        variables.put("coupleSavings", Map.of("value", creditInfoDTO.getCoupleSavings(), "type", "Long"));
        variables.put("applicantCouple", Map.of("value", coupleId, "type", "Long"));

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Realizar la solicitud POST a Camunda
        String camundaUrl = "http://localhost:9000/engine-rest/process-definition/key/MSGF-Test/start";
        try {
            // Realizar la solicitud POST a Camunda
            ResponseEntity<Void> response = restTemplate.postForEntity(camundaUrl, requestEntity, Void.class);
            System.out.println("Camunda process instance started");
            System.out.println(response);
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error en la solicitud a Camunda: " + errorMessage);
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

}
