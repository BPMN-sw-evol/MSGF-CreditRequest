package com.msgfoundation.creditRequest.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CREDIT_REQUEST")
@Data
public class CreditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codRequest")
    private Long codRequest;
    @Column(name = "marriageYears")
    private Long marriageYears;
    @Column(name = "bothEmployees")
    private Boolean bothEmployees;
    @Column(name = "housePrices")
    private Float housePrices;
    @Column(name = "quotaValue")
    private Float quotaValue;
    @Column(name = "coupleSavings")
    private Float coupleSavings;
    @Column(name = "financialViability")
    private Boolean financialViability;
    @Column(name = "isValid")
    private Boolean isValid;
    @JoinColumn(name = "FK_COUPLE")
    @OneToOne(optional = false, targetEntity = Couple.class, cascade = CascadeType.ALL)
    private Couple applicantCouple;
}
