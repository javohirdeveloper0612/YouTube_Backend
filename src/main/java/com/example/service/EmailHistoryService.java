package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.mappers.EmailHistoryMapper;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository repository ;
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
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmailHistoryMapper> pageObj = repository.getListByEmail(pageable , email);

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
}
