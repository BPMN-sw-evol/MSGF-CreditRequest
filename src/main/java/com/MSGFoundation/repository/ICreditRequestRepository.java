package com.MSGFoundation.repository;

import com.MSGFoundation.model.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}