package com.msgfoundation.creditRequest.controller;

import com.msgfoundation.creditRequest.dto.CoupleDTO;
import com.msgfoundation.creditRequest.service.ImpCoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
public class CoupleController {
    @Autowired
    private ImpCoupleService coupleService;

    @PostMapping("/create")
    public ResponseEntity<CoupleDTO> createCouple(@RequestBody CoupleDTO coupleDTO){
        CoupleDTO createdCouple = coupleService.createCouple(coupleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCouple);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CoupleDTO> updateCouple(@PathVariable Long id, @RequestBody CoupleDTO coupleDTO){
        CoupleDTO updatedCouple = coupleService.updateCouple(id, coupleDTO);
        return ResponseEntity.ok(updatedCouple);
    }
}
