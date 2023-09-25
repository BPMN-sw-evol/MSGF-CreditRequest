package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProcessController {
    private final RestTemplate restTemplate;

    public ProcessController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String startProcessInstance(CreditInfoDTO creditInfoDTO){

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
        variables.put("applicantCouple", Map.of("value", creditInfoDTO.getApplicantCoupleId(), "type", "Long"));

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Realizar la solicitud POST a Camunda
        String camundaUrl = "http://localhost:9000/engine-rest/process-definition/key/MSGF-Test/start";
        try {
            // Realizar la solicitud POST a Camunda
            ResponseEntity<Map> response = restTemplate.postForEntity(camundaUrl, requestEntity, Map.class);
            String processId = String.valueOf(response.getBody().get("id"));
            System.out.println("Camunda process instance started with ID: "+processId);
            return processId;
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error en la solicitud a Camunda: " + errorMessage);
            return null;
        }
    }
}
