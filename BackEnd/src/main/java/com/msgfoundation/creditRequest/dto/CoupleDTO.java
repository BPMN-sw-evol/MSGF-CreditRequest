package com.msgfoundation.creditRequest.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CoupleDTO {
    private Long id;
    private String partner1Name;
    private String partner2Name;
}
