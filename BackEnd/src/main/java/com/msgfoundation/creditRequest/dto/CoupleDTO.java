package com.msgfoundation.creditRequest.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CoupleDTO {
    private String partner1Id;
    private String partner2Id;
}
