package com.example.controller;

import com.example.dto.LoginDTO;
import com.example.dto.LoginResponseDTO;
import com.example.dto.ProfileResponseDTO;
import com.example.dto.UserRegistrationDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Access;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@Tag(name = "Authorization Controller", description = "This controller for authorization")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "Method for registration", description = "This method used to create a user")
    private ResponseEntity<ProfileResponseDTO> registration(@Valid @RequestBody UserRegistrationDTO dto , @RequestHeader(value = "Accept-language",defaultValue = "UZ")Language language){
        //log yozilishi kerak

        ProfileResponseDTO responseDTO = authService.registration(dto,language);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(summary = "Method fro verification", description = "This method used to verifying by email after registration")
    @GetMapping("/verification/email/{jwt}")
    private ResponseEntity<String> verification(@PathVariable("jwt") String jwt) {

        String result = authService.verification(jwt);
        return ResponseEntity.ok(result);
    }
/*    @Operation(summary = "Method for authorization", description = "This method used for Login")
    @PostMapping("/authorization")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {


        log.info(" Login :  email {} ", dto.getEmail());
        LoginResponseDTO response = service.login(dto, language);
        return ResponseEntity.ok(response);
    }*/

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto,@RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language language){
        LoginResponseDTO responseDTO = authService.login(dto,language);

        return ResponseEntity.ok(responseDTO);
    }
}
