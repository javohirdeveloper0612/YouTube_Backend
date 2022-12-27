package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.service.EmailHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/email_history")
public class EmailHistoryController {

    private final EmailHistoryService service  ;

    public EmailHistoryController(EmailHistoryService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Email get by admin  method", description = "This method used to create email from only admin")
    @PostMapping("/getEmailByAdmin")
    public ResponseEntity<?> getEmailPaginationByAdmin(@RequestParam(value = "page", defaultValue = "0") Integer page , @RequestParam( value = "size" , defaultValue = "3") Integer size){
        Page<EmailHistoryDTO> result = service.getPageList(page , size);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    @Operation(summary = "Email get by email  method", description = "This method used to create email from only own email address")
    @PostMapping("/getEmail")
    public ResponseEntity<?> getEmailPagination(@RequestParam(value = "page", defaultValue = "0") Integer page ,
                                                @RequestParam( value = "size" , defaultValue = "3") Integer size , @RequestParam String email){
        Page<EmailHistoryDTO> result = service.getPageListWithEmail(page, size , email);
        return ResponseEntity.ok(result);
    }


}
