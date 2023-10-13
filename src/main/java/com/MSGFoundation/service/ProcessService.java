package com.MSGFoundation.service;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.CreditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessService {
    private final RestTemplate restTemplate;

    private final CreditRequestService creditRequestService;

    @Value("${camunda.url.start}")
    private String camundaStartUrl;

    @Autowired
    public ProcessService(RestTemplate restTemplate, CreditRequestService creditRequestService) {
        this.restTemplate = restTemplate;
        this.creditRequestService = creditRequestService;
    }

    public String startProcessInstance(CreditInfoDTO creditInfoDTO){

        // Construir el cuerpo de la solicitud para Camunda
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String coupleName1 = creditInfoDTO.getPeople().get(0).getFullname();
        String coupleName2 = creditInfoDTO.getPeople().get(1).getFullname();

        // Crear un mapa para los atributos que deseas enviar
        Map<String, Object> variables = new HashMap<>();
        variables.put("marriageYears", Map.of("value", creditInfoDTO.getMarriageYears(), "type", "Long"));
        variables.put("bothEmployees", Map.of("value", creditInfoDTO.getBothEmployees(), "type", "Boolean"));
        variables.put("applicantCouple", Map.of("value", creditInfoDTO.getApplicantCoupleId(), "type", "Long"));
        variables.put("coupleName1", Map.of("value", coupleName1, "type", "String"));
        variables.put("coupleName2", Map.of("value", coupleName2, "type", "String"));
        variables.put("creationDate", Map.of("value", String.valueOf(creditInfoDTO.getRequestDate()),"type","String"));

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Realizar la solicitud POST a Camunda
            ResponseEntity<Map> response = restTemplate.postForEntity(camundaStartUrl, requestEntity, Map.class);
            String processId = String.valueOf(response.getBody().get("id"));
            String taskId = getTaskIdByProcessId(processId);
            setAssignee(taskId,"MarriedCouple");
            return processId;
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            return null;
        }
    }

    public void setAssignee(String taskId, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/assignee";

        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Assignee set successfully");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error in the Camunda request: " + errorMessage);
        }
    }

    public Map<String, String> getTaskInfoByProcessId(String processId) {
        // Construir la URL para consultar las tareas relacionadas con el proceso
        String camundaUrl = "http://localhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            // Realizar una solicitud GET a Camunda para obtener la lista de tareas
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {});

            // Verificar si la respuesta contiene tareas
            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
                // Supongamos que tomamos la primera tarea encontrada
                Map<String, String> taskInfo = new HashMap<>();
                taskInfo.put("taskId", String.valueOf(tasks.get(0).get("id")));
                taskInfo.put("taskName", String.valueOf(tasks.get(0).get("name")));
                //taskInfo.put("assignee", String.valueOf(tasks.get(0).get("assignee")));

                System.out.println("Task Info for Process ID " + processId + ":");
                System.out.println("Task ID: " + taskInfo.get("taskId"));
                System.out.println("Task Name: " + taskInfo.get("taskName"));
                //System.out.println("Assignee: " + taskInfo.get("assignee"));

                return taskInfo;
            } else {
                System.err.println("No tasks found for Process ID " + processId);
                return null;
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error en la solicitud a Camunda: " + errorMessage);
            return null;
        }
    }

    public String getTaskIdByProcessId(String processId) {
        Map<String, String> taskInfo = getTaskInfoByProcessId(processId);
        if (taskInfo != null) {
            return taskInfo.get("taskId");
        } else {
            return null;
        }
    }

    public String getTaskNameByProcessId(String processId) {
        Map<String, String> taskInfo = getTaskInfoByProcessId(processId);
        if (taskInfo != null) {
            return taskInfo.get("taskName");
        } else {
            return null;
        }
    }

    public String completeTask(String processId) {
        // Obtener la información de la tarea a partir del Process ID
        Map<String, String> taskInfo = getTaskInfoByProcessId(processId);

        if (taskInfo != null) {
            // Extraer el Task ID de la información de la tarea
            String taskId = taskInfo.get("taskId");
            System.out.println("este es el taskid a completar: "+taskId);

            // Construir el cuerpo de la solicitud para Camunda
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear el cuerpo de la solicitud (sin variables)
            Map<String, Object> requestBody = new HashMap<>();

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Realizar la solicitud POST a Camunda
            String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/complete";
            try {
                // Realizar la solicitud POST a Camunda
                ResponseEntity<Map> response = restTemplate.postForEntity(camundaUrl, requestEntity, Map.class);
                String taskId1 = getTaskIdByProcessId(processId);
                setAssignee(taskId1,"CreditAnalyst");
                CreditRequest creditRequest = creditRequestService.getCreditRequestByProcessId(processId);
                System.out.println(creditRequest.getCodRequest());
                String taskName = getTaskNameByProcessId(creditRequest.getProcessId());
                creditRequest.setStatus(taskName);
                LocalDateTime currentDate = LocalDateTime.now();
                creditRequest.setRequestDate(currentDate);
                creditRequestService.updateCreditRequest(creditRequest.getCodRequest(),creditRequest);
                return String.valueOf(creditRequest.getApplicantCouple().getId());
            } catch (HttpClientErrorException e) {
                String errorMessage = e.getResponseBodyAsString();
                System.err.println("Error en la solicitud a Camunda: " + errorMessage);
                return null;
            }
        } else {
            System.err.println("No se pudo obtener información de la tarea para Process ID " + processId);
            return null;
        }
    }
}
