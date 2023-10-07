package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final CreditRequestController creditRequestController;
    private final CoupleController coupleController;
    private final PersonController personController;

    @Autowired
    public HomeController(CreditRequestController creditRequestController, CoupleController coupleController, PersonController personController) {
        this.creditRequestController = creditRequestController;
        this.coupleController = coupleController;
        this.personController = personController;
    }

    @GetMapping({"/home",""})
    public String mainView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "inicio";
    }

    @GetMapping("/register-credit")
    public String registerFormView(Model model){
        List<Person> people = new ArrayList<>();
        people.add(new Person());
        people.add(new Person());
        model.addAttribute("people",people);

        return "views/creditForm";
    }

    @GetMapping("/view-credit")
    public String registerCreditView(@RequestParam(name = "coupleId", required = false) Long coupleId, Model model){
        if(coupleId==null) {
            coupleId = 3L;
        }
        Couple couple = coupleController.getCoupleById(coupleId);
        List<CreditRequest> creditInfo = creditRequestController.findCreditRequestByCouple(coupleId).getBody();

        String idPartner1 = couple.getPartner1().getId();
        String idPartner2 = couple.getPartner2().getId();

        Person partner1 = personController.getPersonById(idPartner1);
        Person partner2 = personController.getPersonById(idPartner2);

        List<Person> people = new ArrayList<>();
        people.add(partner1);
        people.add(partner2);

        model.addAttribute("people",people);
        model.addAttribute("creditInfo",creditInfo);


        return "views/listCredit";
    }
}
