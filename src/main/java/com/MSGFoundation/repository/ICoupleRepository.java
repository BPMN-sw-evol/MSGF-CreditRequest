package com.MSGFoundation.repository;

import com.MSGFoundation.model.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoupleRepository extends JpaRepository<Couple, Long> {
}
