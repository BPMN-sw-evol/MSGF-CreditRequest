package com.example.CreditRequest.controller;

import com.example.CreditRequest.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping({"/", ""})
    public String mainView() {
        return "index";
    }

    @GetMapping("/register")
    public String registerFormView(Model model){
        List<Person> people = new ArrayList<>();
        people.add(new Person());
        people.add(new Person());
        model.addAttribute("people",people);
        return "registerForm";
    }
}
