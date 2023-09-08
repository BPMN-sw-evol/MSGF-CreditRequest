package com.msgfoundation.creditRequest.service;

import com.msgfoundation.creditRequest.dto.CoupleDTO;

public interface ICoupleService {
    public CoupleDTO createCouple(CoupleDTO coupleDTO);

    public CoupleDTO updateCouple(Long id, CoupleDTO coupleDTO);
}
