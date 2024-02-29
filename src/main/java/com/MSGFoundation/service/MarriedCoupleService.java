package com.MSGFoundation.service;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.dto.TaskInfo;
import com.MSGFoundation.model.CreditRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msgfoundation.annotations.BPMNGetterVariables;
import com.msgfoundation.annotations.BPMNSetterVariables;
import com.msgfoundation.annotations.BPMNTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@BPMNTask(type = "usertask", name = "Diligenciar formulario de solicitud")
public class MarriedCoupleService {
    private final RestTemplate restTemplate;
    private final CreditRequestService creditRequestService;

    @Value("${camunda.url}")
    private String camundaUrl;
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUser;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    private List<TaskInfo> tasksList = new ArrayList<>();

    @Autowired
    public MarriedCoupleService(RestTemplate restTemplate, CreditRequestService creditRequestService) {
        this.restTemplate = restTemplate;
        this.creditRequestService = creditRequestService;
    }

    @BPMNSetterVariables(container = "creditInfoDTO", variables = {"codRequest", "marriageYears", "bothEmployees", "applicantCouple",
            "coupleName1", "coupleName2", "coupleEmail1", "coupleEmail2", "creationDate", "countReviewsBpm"})
    public String startProcessInstance(CreditInfoDTO creditInfoDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String coupleName1 = creditInfoDTO.getPeople().get(0).getFullname();
        String coupleName2 = creditInfoDTO.getPeople().get(1).getFullname();

        String coupleEmail1 = creditInfoDTO.getPeople().get(0).getEmail();
        String coupleEmail2 = creditInfoDTO.getPeople().get(1).getEmail();

        Map<String, Object> variables = new HashMap<>();
        variables.put("codRequest", Map.of("value", creditInfoDTO.getCodRequest(), "type", "Long"));
        variables.put("marriageYears", Map.of("value", creditInfoDTO.getMarriageYears(), "type", "Long"));
        variables.put("bothEmployees", Map.of("value", creditInfoDTO.getBothEmployees(), "type", "Boolean"));
        variables.put("applicantCouple", Map.of("value", creditInfoDTO.getApplicantCoupleId(), "type", "Long"));
        variables.put("coupleName1", Map.of("value", coupleName1, "type", "String"));
        variables.put("coupleName2", Map.of("value", coupleName2, "type", "String"));
        variables.put("coupleEmail1", Map.of("value", coupleEmail1, "type", "String"));
        variables.put("coupleEmail2", Map.of("value", coupleEmail2, "type", "String"));
        variables.put("creationDate", Map.of("value", String.valueOf(creditInfoDTO.getRequestDate()), "type", "String"));
        variables.put("countReviewsBpm", Map.of("value", 0, "type", "Long"));
        variables.put("pdfSupport", Map.of("value", creditInfoDTO.getPdfSupportName(), "type", "String"));
        variables.put("workSupport", Map.of("value", creditInfoDTO.getWorkSupportName(), "type", "String"));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(camundaUrl+"process-definition/key/MSGF-CreditRequest/start", requestEntity, Map.class);
            String processId = String.valueOf(response.getBody().get("id"));
            TaskInfo taskInfo = getTaskInfoByProcessIdWithApi(processId);
            setAssignee(taskInfo.getTaskId(), "MarriedCouple");
            taskInfo.setProcessId(processId);

            return processId;

        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAsString();
        }
    }

    public void setAssignee(String taskId, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl+"/task/"+taskId+"/assignee", HttpMethod.POST, requestEntity, String.class);
            System.out.println("Assignee set successfully");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error in the Camunda request: " + errorMessage);
        }
    }

    public TaskInfo getTaskInfoByProcessId(String processId) {

        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl+"/task?task?processInstanceId="+processId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });

            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
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
            System.err.println("Error with Camunda request: " + errorMessage);
            return null;
        }
    }


    public TaskInfo getTaskInfoByProcessIdWithApi(String processId) {

        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl+"task?processInstanceId="+processId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
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
            System.err.println("Error with Camunda request: " + errorMessage);
            return null;
        }
    }

    @BPMNSetterVariables(variables = {"marriageYears", "bothEmployees", "applicantCouple",
            "coupleName1", "coupleName2", "creationDate", "codRequest"})
    public String updateProcessVariables(String processId, CreditRequest creditRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> modifications = new HashMap<>();
        modifications.put("coupleName1", Map.of("value", creditRequest.getApplicantCouple().getPartner1().getFullname(), "type", "String"));
        modifications.put("coupleEmail1", Map.of("value",creditRequest.getApplicantCouple().getPartner1().getEmail()));
        modifications.put("coupleName2", Map.of("value", creditRequest.getApplicantCouple().getPartner2().getFullname(), "type", "String"));
        modifications.put("coupleEmail2", Map.of("value",creditRequest.getApplicantCouple().getPartner2().getEmail()));
        modifications.put("creationDate", Map.of("value", creditRequest.getRequestDate().toString(), "type", "String"));
        modifications.put("codRequest", Map.of("value", creditRequest.getCodRequest(), "type", "Long"));
        modifications.put("applicantCouple", Map.of("value", creditRequest.getApplicantCouple().getId(), "type", "Long"));
        modifications.put("coupleSavings", Map.of("value", creditRequest.getCoupleSavings(), "type", "Long"));
        modifications.put("marriageYears", Map.of("value", creditRequest.getMarriageYears(), "type", "Long"));
        modifications.put("bothEmployees", Map.of("value", creditRequest.getBothEmployees(), "type", "Boolean"));
        modifications.put("pdfSupport", Map.of("value", creditRequest.getPdfSupport(), "type", "String"));
        modifications.put("workSupport", Map.of("value", creditRequest.getWorkSupport(), "type", "String"));


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("modifications", modifications);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl+"process-instance/"+processId+"/variables", HttpMethod.POST, requestEntity, String.class);
            System.out.println("Variables updated successfully: " + response.getBody());
            return String.valueOf(creditRequest.getApplicantCouple().getId());
        } catch (Exception e) {
            System.err.println("Error while updating variables: " + e.getMessage());
        }
        return "";
    }

    public String completeTask(String processId) {
        TaskInfo taskInfo = getTaskInfoByProcessId(processId);

        if (taskInfo != null) {
            String taskId = taskInfo.getTaskId();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(camundaUrl+"/task/"+taskId+"/complete", requestEntity, Map.class);
                TaskInfo taskInfo1 = getTaskInfoByProcessIdWithApi(processId);
                setAssignee(taskInfo1.getTaskId(), "CreditAnalyst");
                updateReviewAndStatus(processId,"Revisar detalles de solicitud");
                CreditRequest creditRequest = creditRequestService.getCreditRequestByProcessId(processId);
                creditRequest.setStatus(taskInfo1.getTaskName());
                LocalDateTime currentDate = LocalDateTime.now();
                creditRequest.setRequestDate(currentDate);
                creditRequestService.updateCreditRequest(creditRequest.getCodRequest(), creditRequest);
                return String.valueOf(creditRequest.getApplicantCouple().getId());
            } catch (HttpClientErrorException e) {
                String errorMessage = e.getResponseBodyAsString();
                System.err.println("Error with Camunda request: " + errorMessage);
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Could not get task information for process ID: " + processId);
            return null;
        }
    }

    public void messageEvent(String processId) {
        String messageName = "hayIncosistencias";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("inconsistenciasSubsanadas", Map.of("value", true, "type", "boolean"));

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("messageName", messageName);
        requestBodyMap.put("businessKey", processId);
        requestBodyMap.put("processVariables", processVariables);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
        } catch (JsonProcessingException e) {
            System.err.println("error converting request body to JSON");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(camundaUrl+"/message", HttpMethod.POST, requestEntity, String.class);
            System.out.println("Message event done. BusinessID: "+processId);
            updateReviewAndStatus(processId,"Revisar detalles de solicitud");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error with Camunda request: " + errorMessage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getCountReview(String processId){
        CreditRequest creditRequest = creditRequestService.getCreditRequestByProcessId(processId);
        return creditRequest.getCountReviewCR();

    }

    public void updateReviewAndStatus(String processId, String status) throws SQLException {
        System.out.println("database url: "+databaseUrl);
        System.out.println("database user: "+databaseUser);
        System.out.println("database passwprd: "+databasePassword);
        Connection connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);

        String updateQuery = "UPDATE credit_request SET status = ? WHERE process_id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, status);
            updateStatement.setString(2, processId);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Status updated, and count_reviewcr incremented.");
            } else {
                System.out.println("No records found for the given processId: " + processId);
            }
        }
    }
}
