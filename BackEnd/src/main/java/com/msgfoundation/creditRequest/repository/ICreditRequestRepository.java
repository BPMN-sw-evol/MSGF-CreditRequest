package com.msgfoundation.creditRequest.repository;

import com.msgfoundation.creditRequest.model.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
