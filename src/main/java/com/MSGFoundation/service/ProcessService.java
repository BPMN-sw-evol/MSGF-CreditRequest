package com.MSGFoundation.service;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.dto.TaskInfo;
import com.MSGFoundation.model.CreditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessService {
    private final RestTemplate restTemplate;
    private final CreditRequestService creditRequestService;

    @Value("${camunda.url.start}")
    private String camundaStartUrl;

    private List<TaskInfo> tasksList = new ArrayList<>();

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

        String coupleEmail1 = creditInfoDTO.getPeople().get(0).getEmail();
        String coupleEmail2 = creditInfoDTO.getPeople().get(1).getEmail();

        // Crear un mapa para los atributos que deseas enviar
        Map<String, Object> variables = new HashMap<>();
        variables.put("codRequest",Map.of("value", creditInfoDTO.getCodRequest(), "type", "Long"));
        variables.put("marriageYears", Map.of("value", creditInfoDTO.getMarriageYears(), "type", "Long"));
        variables.put("bothEmployees", Map.of("value", creditInfoDTO.getBothEmployees(), "type", "Boolean"));
        variables.put("applicantCouple", Map.of("value", creditInfoDTO.getApplicantCoupleId(), "type", "Long"));
        variables.put("coupleName1", Map.of("value", coupleName1, "type", "String"));
        variables.put("coupleName2", Map.of("value", coupleName2, "type", "String"));
        variables.put("coupleEmail1", Map.of("value", coupleEmail1, "type", "String"));
        variables.put("coupleEmail2", Map.of("value", coupleEmail2, "type", "String"));
        variables.put("creationDate", Map.of("value", String.valueOf(creditInfoDTO.getRequestDate()),"type","String"));

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Realizar la solicitud POST a Camunda
            ResponseEntity<Map> response = restTemplate.postForEntity(camundaStartUrl, requestEntity, Map.class);
            String processId = String.valueOf(response.getBody().get("id"));
            TaskInfo taskInfo = getTaskInfoByProcessIdWithApi(processId);
            setAssignee(taskInfo.getTaskId(),"MarriedCouple");
            taskInfo.setProcessId(processId);
            System.out.println("taskinfo info info: "+taskInfo.toString());



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

    public TaskInfo getTaskInfoByProcessId(String processId) {
        // Construir la URL para consultar las tareas relacionadas con el proceso
        String camundaUrl = "http://localhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            // Realizar una solicitud GET a Camunda para obtener la lista de tareas
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });

            // Verificar si la respuesta contiene tareas
            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
                // Supongamos que tomamos la primera tarea encontrada
                Map<String, String> taskInfoMap = new HashMap<>();
                taskInfoMap.put("taskId", String.valueOf(tasks.get(0).get("id")));
                taskInfoMap.put("taskName", String.valueOf(tasks.get(0).get("name")));
                taskInfoMap.put("assignee", String.valueOf(tasks.get(0).get("assignee")));

                System.out.println("Task Info for Process ID " + processId + ":");
                System.out.println("Task ID: " + taskInfoMap.get("taskId"));
                System.out.println("Task Name: " + taskInfoMap.get("taskName"));
                System.out.println("Assignee: " + taskInfoMap.get("assignee"));

                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setProcessId(processId);
                taskInfo.setTaskId(taskInfoMap.get("taskId"));
                taskInfo.setTaskName(taskInfoMap.get("taskName"));
                taskInfo.setTaskAssignee(taskInfoMap.get("assignee"));

                tasksList.add(taskInfo);
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
        for (TaskInfo taskInfo : tasksList) {
            if (taskInfo.getProcessId().equals(processId)) {
                return taskInfo.getTaskId();
            }
        }
        return null;
    }

    public String getTaskNameByProcessId(String processId) {
        for (TaskInfo taskInfo : tasksList) {
            if (taskInfo.getProcessId().equals(processId)) {
                return taskInfo.getTaskName();
            }
        }
        return null;
    }

    public TaskInfo getTaskInfoByProcessIdWithApi(String processId) {
        String camundaUrl = "http://localhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });
            List<Map> tasks = response.getBody();

            if (tasks != null && !tasks.isEmpty()) {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setTaskId(String.valueOf(tasks.get(0).get("id")));
                taskInfo.setTaskName(String.valueOf(tasks.get(0).get("name")));
                taskInfo.setTaskAssignee(String.valueOf(tasks.get(0).get("assignee")));

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

    public String updateProcessVariables(String processId, CreditRequest creditRequest) {
        String camundaUrl = "http://localhost:9000/engine-rest/process-instance/" + processId + "/variables";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> modifications = new HashMap<>();
        modifications.put("marriageYears", Map.of("value", creditRequest.getMarriageYears(), "type", "Long"));
        modifications.put("bothEmployees", Map.of("value", creditRequest.getBothEmployees(), "type", "Boolean"));
        modifications.put("applicantCouple", Map.of("value", creditRequest.getApplicantCouple().getId(), "type", "Long"));
        modifications.put("coupleName1", Map.of("value", creditRequest.getApplicantCouple().getPartner1().getFullname(), "type", "String"));
        modifications.put("coupleName2", Map.of("value", creditRequest.getApplicantCouple().getPartner2().getFullname(), "type", "String"));
        modifications.put("creationDate", Map.of("value", creditRequest.getRequestDate().toString(), "type", "String"));
        modifications.put("codRequest", Map.of("value", creditRequest.getCodRequest(), "type", "Long"));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("modifications", modifications);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Variables updated successfully: " + response.getBody());
            return String.valueOf(creditRequest.getApplicantCouple().getId());
        } catch (Exception e) {
            System.err.println("Error while updating variables: " + e.getMessage());
        }
        return "";
    }

    public String completeTask(String processId) {
        // Obtener la información de la tarea a partir del Process ID
        TaskInfo taskInfo = getTaskInfoByProcessId(processId);

        if (taskInfo != null) {
            // Extraer el Task ID de la información de la tarea
            String taskId = taskInfo.getTaskId();
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
                TaskInfo taskInfo1 = getTaskInfoByProcessIdWithApi(processId);
                setAssignee(taskInfo1.getTaskId(),"CreditAnalyst");
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
