package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.EmailResponseHistoryDTO;
import com.example.enums.Language;
import com.example.service.EmailHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email_history")
@Tag(name = "emailHistoryController",description = "this controller for email history")
public class EmailHistoryController {

    private final EmailHistoryService service;

    public EmailHistoryController(EmailHistoryService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "getByEmail",description = "This method get history by email (only ADMIN)")
    @GetMapping("/get_by_email")

    public ResponseEntity<?> getByEmail(@RequestBody EmailResponseHistoryDTO dto,
                                        @RequestHeader(name = "Accept-Language",defaultValue = "UZ")Language language){
        List<EmailResponseHistoryDTO> result = service.geByEmail(dto.getEmail(),language);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Method for get by date", description = "This method used to get email history by date")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getByDate(@PathVariable("date") String date,
                                       @RequestHeader(name = "Accept-Language",defaultValue = "UZ")Language language) {
        List<EmailResponseHistoryDTO> result = service.getByDate(date,language);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Method for get list", description = "This method used to get all history list")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                     @RequestParam(name = "size",defaultValue = "1") Integer size) {
        Page<EmailResponseHistoryDTO> result = service.getList(page, size);
        return ResponseEntity.ok(result);

    }
}
