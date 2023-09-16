package com.example.CreditRequest.service;

import com.example.CreditRequest.model.Couple;
import com.example.CreditRequest.repository.ICoupleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoupleService {
    private final ICoupleRepository coupleRepository;

    @Autowired
    public CoupleService(ICoupleRepository coupleRepository) {
        this.coupleRepository = coupleRepository;
    }

    public List<Couple> getAllCouples() {
        return coupleRepository.findAll();
    }

    public Couple getCoupleById(Long id) {
        Optional<Couple> optionalCouple = coupleRepository.findById(id);
        return optionalCouple.orElse(null);
    }

    public Couple createCouple(Couple couple) {
        return coupleRepository.save(couple);
    }

    public Couple updateCouple(Long id, Couple couple) {
        if (coupleRepository.existsById(id)) {
            couple.setId(id);
            return coupleRepository.save(couple);
        }
        return null; // Handle not found scenario
    }

    public void deleteCouple(Long id) {
        coupleRepository.deleteById(id);
    }
}
