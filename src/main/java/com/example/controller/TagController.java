package com.example.controller;

import com.example.dto.TagCreateDTO;
import com.example.dto.TagDTO;
import com.example.dto.TagResponseDTO;
import com.example.enums.Language;
import com.example.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Tag(name = "TagController",description = "this Tag controller")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }



    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Tag create",description = "This mehtod create new tag")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid  @RequestBody TagCreateDTO dto ,
                                    @RequestHeader(name = "Accept-Language",defaultValue = "UZ")Language language ){
        TagResponseDTO result = service.create(dto,language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tag update method", description = " this method update tag by id (only ADMIN) ")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody TagCreateDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {

        String result = service.updateById(id, dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tag delete method", description = " this method delete tag by id (only ADMIN)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {

        String result = service.deleteById(id, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Tag List", description = "this method get list tag")
    @GetMapping("/list")
    public ResponseEntity<List<TagResponseDTO>> getList() {
        List<TagResponseDTO> result = service.tagList();
        return ResponseEntity.ok(result);
    }
}
