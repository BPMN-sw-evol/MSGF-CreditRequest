package com.example.CreditRequest.repository;

import com.example.CreditRequest.model.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoupleRepository extends JpaRepository<Couple, Long> {
}
