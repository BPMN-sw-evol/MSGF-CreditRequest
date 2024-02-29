package com.MSGFoundation.service;

import com.MSGFoundation.dto.CoupleDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.repository.ICoupleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoupleService {
    private final ICoupleRepository coupleRepository;
    private final PersonService personService;

    public List<Couple> getAllCouples() {
        return coupleRepository.findAll();
    }

    public Couple getCoupleById(Long id) {
        Optional<Couple> optionalCouple = coupleRepository.findById(id);
        return optionalCouple.orElse(null);
    }

    public Couple createCouple(CoupleDTO coupleDTO) {
        String partner1Id = coupleDTO.getPartner1Id();
        String partner2Id = coupleDTO.getPartner2Id();

        Person partner1 = personService.getPersonById(partner1Id);
        Person partner2 = personService.getPersonById(partner2Id);

        Couple couple = new Couple();
        couple.setPartner1(partner1);
        couple.setPartner2(partner2);

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

    public Long getCouplebyIds(String partnerId1, String partnerId2) {
        Couple couple = coupleRepository.findByPartner1IdAndPartner2Id(partnerId1,partnerId2);
        if(couple != null){
            return couple.getId();
        }
        else{
            return null;
        }
    }
}
