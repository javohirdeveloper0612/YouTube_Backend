package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.mappers.EmailHistoryMapper;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailHistoryService {

    private final EmailHistoryRepository repository ;

    public EmailHistoryService(EmailHistoryRepository repository) {
        this.repository = repository;
    }

    public Page<EmailHistoryDTO> getPageList(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmailHistoryMapper> pageObj = repository.getList(pageable);

        List<EmailHistoryDTO> dtoList = new ArrayList<>();

        for (EmailHistoryMapper emailHistoryMapper : pageObj) {
            EmailHistoryDTO dto = new EmailHistoryDTO();
            dto.setId(emailHistoryMapper.getId());
            dto.setTo_email(emailHistoryMapper.getTo_email());
            dto.setTitle(emailHistoryMapper.getTitle());
            dto.setMessage(emailHistoryMapper.getMessage());
            dto.setCreatedDate(emailHistoryMapper.getCreatedDate());
            dtoList.add(dto) ;
        }


        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public Page<EmailHistoryDTO> getPageListWithEmail(Integer page, Integer size, String email) {
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<EmailHistoryMapper> pageObj = repository.getListByEmail(pageable , email);
//
//        List<EmailHistoryDTO> dtoList = new ArrayList<>();
//
//        for (EmailHistoryMapper emailHistoryMapper : pageObj) {
//            EmailHistoryDTO dto = new EmailHistoryDTO();
//            dto.setId(emailHistoryMapper.getId());
//            dto.setTo_email(emailHistoryMapper.getTo_email());
//            dto.setTitle(emailHistoryMapper.getTitle());
//            dto.setMessage(emailHistoryMapper.getMessage());
//            dto.setCreatedDate(emailHistoryMapper.getCreatedDate());
//            dtoList.add(dto) ;
//        }
//
//
//        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
        return  null ;
    }

    public void create(EmailHistoryEntity entity) {

        EmailHistoryEntity save = repository.save(entity);


    }

    public Long getCountInMinute(String email) {

        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = toDate.minusMinutes(1);

        return repository.countByTo_emailAndCreatedDateBetween(email, fromDate, toDate);
    }
}
