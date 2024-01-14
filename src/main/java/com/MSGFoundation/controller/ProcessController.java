package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.service.MarriedCoupleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProcessController {
    private final MarriedCoupleService marriedCoupleService;

    public ProcessController(MarriedCoupleService marriedCoupleService) {
        this.marriedCoupleService = marriedCoupleService;
    }

    @PostMapping("/startProcess")
    public String startProcessInstance(@ModelAttribute CreditInfoDTO creditInfoDTO) {
        this.marriedCoupleService.startProcessInstance(creditInfoDTO);
        return "redirect:/view-credit";
    }

    @GetMapping("/complete")
    public String completeTask(@RequestParam(name = "taskId") String taskId) {
        String resultado = this.marriedCoupleService.completeTask(taskId);
        return "redirect:/view-credit?coupleId="+resultado;
    }

    @GetMapping("/message-event")
    public void messageEvent(){
        marriedCoupleService.messageEvent();
    }
}
