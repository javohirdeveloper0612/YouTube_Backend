package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.EmailHistoryDTO;
import com.example.dto.EmailResponseHistoryDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.EmailHistoryEntity;
import com.example.enums.Language;
import com.example.exp.email.EmailNotFoundException;
import com.example.exp.email.IncorrectDateFormatException;
import com.example.repository.EmailHistoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailHistoryService {

    private final EmailHistoryRepository repository;

    private final ResourceBundleService resourceBundleService;

    public EmailHistoryService(EmailHistoryRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    public Long getCountInMinute(String email) {

        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusMinutes(1);

        return repository.countByEmailAndCreatedDateBetween(email, fromDate, toDate);
    }

    public void create(EmailHistoryEntity entity) {
        repository.save(entity);
    }

    public List<EmailResponseHistoryDTO> geByEmail(String email, Language language) {
        List<EmailHistoryEntity> list = repository.findByEmail(email);
        if (list.isEmpty()){
            throw new EmailNotFoundException(resourceBundleService.getMessage("email.not.found",language));
        }
List<EmailResponseHistoryDTO> dtoList= new ArrayList<>();

        list.forEach(entity -> dtoList.add(getDTO(entity)));
        return dtoList;
    }

    public EmailResponseHistoryDTO getDTO(EmailHistoryEntity entity){
        EmailResponseHistoryDTO dto = new EmailResponseHistoryDTO();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(dto.getCreatedDate());

        return dto;

    }

    public List<EmailResponseHistoryDTO> getByDate(String date,Language language) {

        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date);
        }catch (IncorrectDateFormatException e){
            throw new IncorrectDateFormatException(resourceBundleService.getMessage("incorrect.date.format",language));
        }

        List<EmailHistoryEntity> entityList = repository.findByCreatedDate(localDate);
        List<EmailResponseHistoryDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(getDTO(entity)));
        return dtoList;
    }

    public Page<EmailResponseHistoryDTO> getList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EmailHistoryEntity> pageObj = repository.findAll(pageable);
        List<EmailHistoryEntity> entityList = pageObj.getContent();

        List<EmailResponseHistoryDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(getDTO(entity)));

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }
}
