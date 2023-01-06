package com.example.youtube.controller;

import com.example.youtube.config.security.CustomUserDetails;

import com.example.youtube.dto.subscription.SubscriptionDTO;
import com.example.youtube.dto.subscription.SubscriptionInfo;
import com.example.youtube.entity.SubscriptionEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.repository.SubscriptionRepository;
import com.example.youtube.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/subscription")
@Tag(name = "Subscription", description = "thes is Control Subscription")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }



    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Subscription create method", description = "User use this method to create Subscription")
    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody SubscriptionDTO dto,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        String result = service.add(dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Subscription changeStatus method", description = "User use this method to changeStatus Subscription")
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Boolean resule = service.changeStatus(id,language);

        return ResponseEntity.ok(resule);

    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Subscription changeNotificationType method", description = "User use this method to changeNotificationType Subscription")
    @GetMapping("/changeType/{id}")
    public ResponseEntity<?> changeNotificationType(@PathVariable Integer id,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Boolean resule = service.changeNotificationType(id,language);
        return ResponseEntity.ok(resule);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Subscription getUserSubscrition method", description = "User use this method to getUserSubscrition Subscription")
    @GetMapping("/getUserSubscrition")
    public ResponseEntity<?> getUserSubscrition( @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        List<SubscriptionInfo> result = service.getUserSubscrition(getUserId());

        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Subscription getUserSubscrition2 method", description = "User use this method to getUserSubscrition2 Subscription")

    @GetMapping("/getUserSubscrition2/{id}")
    public ResponseEntity<?> getUserSubscrition2(@PathVariable Integer id) {


        List<SubscriptionInfo> result = service.getUserSubscrition2(id);

        return ResponseEntity.ok(result);
    }


    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getId();
    }

}
