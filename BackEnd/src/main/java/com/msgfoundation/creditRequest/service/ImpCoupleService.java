package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.dto.CoupleDTO;
import com.msgfoundation.creditRequest.model.Couple;
import com.msgfoundation.creditRequest.repository.ICoupleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImpCoupleService implements ICoupleService{
    @Autowired
    private ICoupleRepository coupleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CoupleDTO createCouple(CoupleDTO coupleDTO) {
        Couple couple = modelMapper.map(coupleDTO, Couple.class);
        System.out.println("Couple has been created");
        return modelMapper.map(coupleRepository.save(couple), CoupleDTO.class);
    }

    @Override
    public CoupleDTO updateCouple(Long id, CoupleDTO coupleDTO) {
        CoupleDTO existingCouple = findById(id);
        return null;
    }

    private CoupleDTO findById(Long id) {
        Optional<Couple> couple = coupleRepository.findById(id);
        if(couple.isEmpty()){
            System.out.println("usuario no encontrado");
        }
        return modelMapper.map(couple, CoupleDTO.class);
    }
}
