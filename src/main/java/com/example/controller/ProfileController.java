package com.example.controller;

import com.example.config.security.CustomUserDetail;
import com.example.dto.AdminRegistrationDTO;
import com.example.dto.ProfileDetailDTO;
import com.example.dto.ProfileResponseDTO;
import com.example.enums.Language;
import com.example.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
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
@Tag(name = "profile controller for profile methods")
public class ProfileController {

        private final ProfileService service;

        public ProfileController(ProfileService service) {
                this.service = service;
        }
        @PreAuthorize("hasRole('USER')")
        @Operation(summary = "changePassword",description = "this method for change password")
        @PutMapping("/changePassword/{pswd}")
        public ResponseEntity<?> changePassword(@PathVariable("pswd") String password,
                                                @RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

                ProfileResponseDTO dto = service.updatePassword(password,customUserDetail.getId(),language);
                log.warn("watwat",dto);

                return ResponseEntity.ok(dto);
        }
@PreAuthorize("hasRole('USER')")
@Operation(summary = "update email" ,description = "this method for update email")
        @PutMapping("/update_email/{email}")
        public ResponseEntity<?> updateEmail(@PathVariable("email")String email,
                                             @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language language){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

                ProfileResponseDTO dto = service.updateEmail(email,user.getId(),language);

                return ResponseEntity.ok(dto);
}

@PreAuthorize("hasRole('USER')")
        @Operation(summary ="update detail",description = "this method for profile detail update")
        @PutMapping("/update_detail")
        public ResponseEntity<?> updateDetail(@RequestBody ProfileDetailDTO dto,
                                              @RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

  ProfileResponseDTO result= service.updateDetail(dto,user.getId(),language);

  return ResponseEntity.ok(result);
}


@PreAuthorize("hasRole('ADMIN')")
@Operation(summary = "create profile",description = "this method for create profile only for ADMIN")
@PostMapping("/create")
public ResponseEntity<?> create(@RequestBody AdminRegistrationDTO dto,
                                @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language language){
                ProfileResponseDTO responseDTO = service.create(dto,language);
                return ResponseEntity.ok(responseDTO);
}

        @PreAuthorize("hasRole('USER')")
        @Operation(summary = "update photo",description = "this method for update profile photo")
        @PutMapping("/update_photo")
        public ResponseEntity<?> updatePhoto(@RequestParam("file")MultipartFile file,
                                             @RequestHeader(value = "Accept-Language",defaultValue = "EN")Language language){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

                ProfileResponseDTO dto = service.updatePhoto(userDetail.getId(),file,language);

                return ResponseEntity.ok(dto);
        }

        @PreAuthorize("hasRole('USER')")
        @Operation(summary = "get detail",description = "this method for get detail")
        @GetMapping("/get_detail")
        public ResponseEntity<?> getDetail(@RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

                ProfileResponseDTO responseDTO = service.getDetail(userDetail.getId(),language);

                return ResponseEntity.ok().body(responseDTO);
        }





}
