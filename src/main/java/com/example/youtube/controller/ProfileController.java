package com.example.youtube.controller;


import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.profile.ProfileResponseDTO;
import com.example.youtube.dto.auth.AdminRegistrationDTO;
import com.example.youtube.dto.profile.ProfileDetailDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/profile")
@Tag(name = "Profile Controller", description = "This controller for profile")
public class ProfileController {


    public final ProfileService service;

    public ProfileController(ProfileService profileService) {
        this.service = profileService;
    }

    @PreAuthorize(value = "hasRole('USER')")
    @Operation(summary = "Method for registration", description = "This method used to create a user")
    @PutMapping("/update_password/{pswd}")
    public ResponseEntity<?> registration(@PathVariable("pswd") String password,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();


        ProfileResponseDTO result = service.updatePassword(password, user.getId(), language);


        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update_email/{email}")
    public ResponseEntity<?> updateEmail(@PathVariable("email") String password,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        ProfileResponseDTO result = service.updateEmail(password, user.getId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update_detail")
    public ResponseEntity<?> updateDetail(@RequestBody ProfileDetailDTO detailDTO, @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();


        ProfileResponseDTO result = service.updateDetail(detailDTO, user.getId(), language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update_photo")
    public ResponseEntity<?> updatePhoto(@RequestParam("file") MultipartFile file, @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        ProfileResponseDTO result = service.updatePhoto(user.getId(), file, language);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get_detail")
    public ResponseEntity<?> getDetail(@RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        ProfileResponseDTO result = service.getDetail(user.getId(), language);
        return ResponseEntity.ok().body(result);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdminRegistrationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        ProfileResponseDTO result = service.create(dto);
        return ResponseEntity.ok(result);
    }
}
