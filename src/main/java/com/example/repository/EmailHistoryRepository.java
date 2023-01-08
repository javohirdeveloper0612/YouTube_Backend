package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity,Integer> {

    long countByEmailAndCreatedDateBetween(String email, LocalDateTime fromDate, LocalDateTime toDate);

    List<EmailHistoryEntity> findByEmail(String email);

    @Query(value = "SELECT * from email_history e where to_date(to_char(e.created_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')=?1", nativeQuery = true)
    List<EmailHistoryEntity> findByCreatedDate(LocalDate createdDate);

}
