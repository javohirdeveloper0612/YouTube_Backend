package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity,Integer> {

    long countByEmailAndCreatedDateBetween(String email, LocalDateTime fromDate, LocalDateTime toDate);

}
