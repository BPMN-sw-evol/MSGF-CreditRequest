package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final CreditRequestController creditRequestController;
    private final CoupleController coupleController;
    @Autowired
    public HomeController(CreditRequestController creditRequestController, CoupleController coupleController) {
        this.creditRequestController = creditRequestController;
        this.coupleController = coupleController;
    }

    @GetMapping({"/", ""})
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
            coupleId = 1L;
        }
        Couple couple = coupleController.getCoupleById(coupleId);
        CreditRequest creditInfo = creditRequestController.findCreditRequestByCouple(coupleId).getBody();

        String idPartner1 = couple.getPartner1().getId();
        String idPartner2 = couple.getPartner2().getId();

        model.addAttribute("idPartner1",idPartner1);
        model.addAttribute("idPartner2",idPartner2);
        if(creditInfo!=null) {

            model.addAttribute("creditCod", creditInfo.getCodRequest());
            model.addAttribute("housePrice", creditInfo.getHousePrices());
            model.addAttribute("quotaValue", creditInfo.getQuotaValue());
            model.addAttribute("requestDate", creditInfo.getRequestDate().getYear()
                    +"-"+creditInfo.getRequestDate().getMonthValue()
                    +"-"+creditInfo.getRequestDate().getDayOfMonth()
                    +" "+creditInfo.getRequestDate().getHour()
                    +":"+creditInfo.getRequestDate().getMinute()
                    +":"+creditInfo.getRequestDate().getSecond());
            model.addAttribute("status", creditInfo.getStatus());
        }

        return "views/listCredit";
    }
}
