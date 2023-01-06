package com.example.youtube.controller;

import com.example.youtube.dto.EmailHistoryResponseDTO;
import com.example.youtube.service.EmailHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email_history")
@Tag(name = "Email History Controller", description = "This Controller for email history")
public class EmailHistoryController {

    private final EmailHistoryService service;

    public EmailHistoryController(EmailHistoryService service) {
        this.service = service;
    }


    @Operation(summary = "Method for get by email", description = "This method used to get history by email")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get_by_email")
    public ResponseEntity<?> getByEmail(@RequestBody EmailHistoryResponseDTO dto) {
        List<EmailHistoryResponseDTO> result = service.getByEmail(dto.getEmail());
        return ResponseEntity.ok(result);

    }

    @Operation(summary = "Method for get by date", description = "This method used to get email history by date")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getByDate(@PathVariable("date") String date) {
        List<EmailHistoryResponseDTO> result = service.getByDate(date);
        return ResponseEntity.ok(result);

    }


    @Operation(summary = "Method for get list", description = "This method used to get all history list")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<EmailHistoryResponseDTO> result = service.getList(page, size);
        return ResponseEntity.ok(result);

    }
}
