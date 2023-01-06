package com.example.service;

import com.example.entity.EmailHistoryEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class EmailHistoryService {

    private final EmailHistoryRepository repository;

    public EmailHistoryService(EmailHistoryRepository repository) {
        this.repository = repository;
    }

    public Long getCountInMinute(String email) {

        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusMinutes(1);

        return repository.countByEmailAndCreatedDateBetween(email, fromDate, toDate);
    }

    public void create(EmailHistoryEntity entity) {
        repository.save(entity);
    }
}
