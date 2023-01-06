package com.example.youtube.controller;

import com.example.youtube.dto.attach.AttachResponseDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
@Tag(name = "Attach Controller", description = "This Controller for attach")
public class AttachController {

    private final AttachService service;

    public AttachController(AttachService service) {
        this.service = service;
    }


    @Operation(summary = "Method for upload", description = "This method used to  upload file")
    @PostMapping("/public/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        AttachResponseDTO fileName = service.saveToSystem(file, language);
        return ResponseEntity.ok().body(fileName);
    }


    @Operation(summary = "Method for open", description = "This method used to  open file")
    @GetMapping(value = "/public/open/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        return service.open(fileName);
    }


    @Operation(summary = "Method for download", description = "This method used to  download file")
    @GetMapping("/public/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Resource file = service.download(fileName, language);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @Operation(summary = "Method for get ", description = "This method used to  get file")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getWithPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<AttachResponseDTO> result = service.getWithPage(page, size);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Method for delete", description = "This method used to  delete file")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<?> deleteById(@PathVariable("fileName") String fileName) {
        String result = service.deleteById(fileName);
        return ResponseEntity.ok(result);
    }


}
