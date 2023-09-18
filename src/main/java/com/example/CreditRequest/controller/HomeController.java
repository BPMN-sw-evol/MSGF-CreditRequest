package com.example.CreditRequest.controller;

import com.example.CreditRequest.model.CreditRequest;
import com.example.CreditRequest.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping({"/", ""})
    public String mainView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "inicio";
    }

    @GetMapping({"/register-couple", "/views/coupleForm.html"})
    public String registerFormView(Model model){
        List<Person> people = new ArrayList<>();
        people.add(new Person());
        people.add(new Person());
        model.addAttribute("people",people);

        return "views/coupleForm";
    }

    @GetMapping("/register-credit")
    public String registerCreditView(Model model){
        return "views/creditForm";
    }
}
