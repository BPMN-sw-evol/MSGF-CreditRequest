package com.MSGFoundation.controller;

import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.CoupleService;
import com.MSGFoundation.service.CreditRequestService;
import com.MSGFoundation.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CreditRequestService creditRequestService;
    private final CoupleService coupleService;
    private final PersonService personService;

    @GetMapping({"/home",""})
    public String mainView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "inicio";
    }

    @GetMapping("/register-credit")
    public String registerInfoFormView(Model model){
        List<Person> people = new ArrayList<>();
        List<CreditRequest> creditInfo = new ArrayList<>();

        people.add(new Person());
        people.add(new Person());

        model.addAttribute("people",people);
        model.addAttribute("creditInfo",creditInfo);

        return "views/creditForm";
    }


    @GetMapping("/view-credit")
    public String registerCreditView(@RequestParam(name = "coupleId", required = false) Long coupleId, Model model){
        if(coupleId==null) {
            CreditRequest creditRequest = creditRequestService.findFirstByOrderByRequestDateDesc();
            coupleId = creditRequest.getApplicantCouple().getId();
        }
        Couple couple = coupleService.getCoupleById(coupleId);
        List<CreditRequest> creditInfo = creditRequestService.findCreditByCouple(couple);

        String idPartner1 = couple.getPartner1().getId();
        String idPartner2 = couple.getPartner2().getId();

        Person partner1 = personService.getPersonById(idPartner1);
        Person partner2 = personService.getPersonById(idPartner2);

        List<Person> people = new ArrayList<>();
        people.add(partner1);
        people.add(partner2);
        model.addAttribute("people",people);
        model.addAttribute("creditInfo",creditInfo);

        return "views/listCredit";
    }
}
