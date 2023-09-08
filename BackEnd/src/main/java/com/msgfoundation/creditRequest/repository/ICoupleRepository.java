package com.msgfoundation.creditRequest.repository;

import com.msgfoundation.creditRequest.model.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoupleRepository extends JpaRepository<Couple, Long> {
}
