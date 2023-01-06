package com.example.youtube.service;

import com.example.youtube.dto.EmailHistoryResponseDTO;
import com.example.youtube.entity.EmailHistoryEntity;
import com.example.youtube.exp.IncorrectDateFormatException;
import com.example.youtube.repository.EmailHistoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailHistoryService {
    private final EmailHistoryRepository repository;

    public EmailHistoryService(EmailHistoryRepository repository) {
        this.repository = repository;
    }


    public void create(EmailHistoryEntity entity) {
        repository.save(entity);
    }

    public List<EmailHistoryResponseDTO> getByEmail(String email) {
        List<EmailHistoryEntity> entityList = repository.findByEmail(email);

        List<EmailHistoryResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(getDTO(entity)));
        return dtoList;

    }

    private EmailHistoryResponseDTO getDTO(EmailHistoryEntity entity) {
        EmailHistoryResponseDTO dto = new EmailHistoryResponseDTO();
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<EmailHistoryResponseDTO> getByDate(String date) {
        LocalDate cd;
        try {
            cd = LocalDate.parse(date);
        } catch (IncorrectDateFormatException e) {
            throw new IncorrectDateFormatException(e.getMessage());
        }

        List<EmailHistoryEntity> entityList = repository.findByCreatedDate(cd);
        List<EmailHistoryResponseDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(getDTO(entity)));
        return dtoList;
    }

    public Page<EmailHistoryResponseDTO> getList(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created_date");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmailHistoryEntity> pageObj = repository.findAll(pageable);
        List<EmailHistoryEntity> entityList = pageObj.getContent();

        List<EmailHistoryResponseDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(getDTO(entity)));

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }


    public Long getCountInMinute(String email) {

        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusMinutes(1);

        return repository.countByEmailAndCreatedDateBetween(email, fromDate, toDate);
    }
}
