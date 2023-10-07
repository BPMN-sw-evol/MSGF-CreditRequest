package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.service.ProcessService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProcessController {
    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/startProcess")
    public String startProcessInstance(@ModelAttribute CreditInfoDTO creditInfoDTO){
        return this.processService.startProcessInstance(creditInfoDTO);
    }

    @GetMapping("/complete")
    public String completeTask(String taskId, boolean isValid) {
        return this.processService.completeTask(taskId,isValid);
    }
}
