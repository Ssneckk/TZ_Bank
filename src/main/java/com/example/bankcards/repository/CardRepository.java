package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    @Modifying
    @Query("UPDATE Card c SET c.status = 'EXPIRED' WHERE c.expireDate< :date")
    int markExpired(@Param("date") LocalDate date);

    Page<Card> findByUserId(Integer userId, Pageable pageable);

    boolean existsByIdAndUserId(Integer cardId, Integer userId);
}
