package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import com.example.mappers.EmailHistoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,Integer> {
    @Query("select new com.example.mappers.EmailHistoryMapper(c.id, c.to_email ,c.title , c.message ,c.createdDate ) from EmailHistoryEntity c ")
    Page<EmailHistoryMapper> getList(Pageable pageable);

    @Query("select new com.example.mappers.EmailHistoryMapper(c.to_email ,c.title , c.message ,c.createdDate ) from EmailHistoryEntity c where c.to_email=?2")
    Page<EmailHistoryMapper> getListByEmail(Pageable pageable, String email);


    long countByTo_emailAndCreatedDateBetween(String email, LocalDateTime fromDate, LocalDateTime toDate);



}
