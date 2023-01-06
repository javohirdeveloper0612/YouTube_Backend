package com.example.controller;

import com.example.dto.AttachResponseDTO;
import com.example.enums.Language;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
@Tag(name = "attach controller",description = "this controller for media")
public class AttachController {

    private final AttachService service;

    public AttachController(AttachService service) {
        this.service = service;
    }

    @PostMapping("/public/upload")
    public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file,
                                    @RequestHeader(value = "Accept-Language",defaultValue = "EN")Language language){
        AttachResponseDTO dto = service.saveAttach(file,language);

        return ResponseEntity.ok().body(dto);
    }
}


