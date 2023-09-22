package com.MSGFoundation.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "status")
    private String status;
    @Column(name = "requestDate")
    private LocalDateTime requestDate;
    @JoinColumn(name = "FK_COUPLE")
    @OneToOne
    private Couple applicantCouple;
}
