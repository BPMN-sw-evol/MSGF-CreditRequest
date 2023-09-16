package com.example.CreditRequest.controller;

import com.example.CreditRequest.dto.CoupleDTO;
import com.example.CreditRequest.model.Couple;
import com.example.CreditRequest.model.Person;
import com.example.CreditRequest.service.CoupleService;
import com.example.CreditRequest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/couple")
public class CoupleController {
    private final CoupleService coupleService;
    private final PersonService personService;

    @Autowired
    public CoupleController(CoupleService coupleService, PersonService personService) {
        this.coupleService = coupleService;
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Couple> getAllCouples() {
        return coupleService.getAllCouples();
    }

    @GetMapping("/{id}")
    public Couple getCoupleById(@PathVariable Long id) {
        return coupleService.getCoupleById(id);
    }

    @PostMapping("/create")
    public Couple createCouple(@RequestBody CoupleDTO coupleDTO) {
        String partner1Id = coupleDTO.getPartner1Id();
        String partner2Id = coupleDTO.getPartner2Id();

        Person partner1 = personService.getPersonById(partner1Id);
        Person partner2 = personService.getPersonById(partner2Id);

        if (partner1 != null && partner2 != null) {
            Couple couple = new Couple();
            couple.setPartner1(partner1);
            couple.setPartner2(partner2);

            return coupleService.createCouple(couple);
        } else {
            // Manejar el escenario en el que una o ambas personas no existen
            return null;
        }
    }

    @PutMapping("/update/{id}")
    public Couple updateCouple(@PathVariable Long id, @RequestBody Couple couple) {
        return coupleService.updateCouple(id, couple);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCouple(@PathVariable Long id) {
        coupleService.deleteCouple(id);
    }
}
