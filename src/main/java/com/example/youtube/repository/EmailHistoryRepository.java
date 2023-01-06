package com.example.youtube.repository;

import com.example.youtube.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer>, PagingAndSortingRepository<EmailHistoryEntity, Integer> {
    List<EmailHistoryEntity> findByEmail(String email);


    @Query(value = "SELECT * from email_history e where to_date(to_char(e.created_date, 'YYYY/MM/DD'), 'YYYY/MM/DD')=?1", nativeQuery = true)
    List<EmailHistoryEntity> findByCreatedDate(LocalDate createdDate);

    long countByEmailAndCreatedDateBetween(String email, LocalDateTime fromDate, LocalDateTime toDate);



}
