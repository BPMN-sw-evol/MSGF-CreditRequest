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
    @Column(name = "processId")
    private String processId;
    @Column(name = "marriageYears")
    private Long marriageYears;
    @Column(name = "bothEmployees")
    private Boolean bothEmployees;
    @Column(name = "housePrices")
    private Long housePrices;
    @Column(name = "quotaValue")
    private Long quotaValue;
    @Column(name = "coupleSavings")
    private Long coupleSavings;
    @Column(name = "status")
    private String status;
    @Column(name = "requestDate")
    private LocalDateTime requestDate;
    @Column(name = "countReviewCR")
    private Long countReviewCR;
    @JoinColumn(name = "FK_COUPLE")
    @OneToOne
    private Couple applicantCouple;

}
