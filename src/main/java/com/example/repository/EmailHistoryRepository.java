package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,Integer> {
}
