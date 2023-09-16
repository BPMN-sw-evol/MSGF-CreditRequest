package com.example.CreditRequest.repository;

import com.example.CreditRequest.model.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
