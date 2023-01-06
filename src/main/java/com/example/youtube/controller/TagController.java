package com.example.youtube.controller;

import com.example.youtube.dto.tag.TagCreateDTO;
import com.example.youtube.dto.tag.TagResponseDTO;
import com.example.youtube.dto.tag.TagUpdateDTO;
import com.example.youtube.enums.Language;
import com.example.youtube.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tag")
@Tag(name = "Channel Controller", description = "This api used to control channel")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Tag create method", description = "User use this method to create tag")
    @PostMapping("/create")
    public ResponseEntity<TagResponseDTO> create(@Valid @RequestBody TagCreateDTO dto) {
        TagResponseDTO result = tagService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tag update method", description = " ADMIN CAN UPDATE TAG NAME ")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id, @RequestBody TagUpdateDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Tag is updating: {}, {}", id, dto.getName());
        Boolean result = tagService.updateById(id, dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tag delete method", description = " ADMIN CAN DELETE TAG ")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        log.info("Tag is deleting: {}", id);
        Boolean result = tagService.deleteById(id, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Tag List", description = "Get Tag List")
    @GetMapping("/list")
    public ResponseEntity<List<TagResponseDTO>> getList() {
        List<TagResponseDTO> result = tagService.tagList();
        return ResponseEntity.ok(result);
    }
}
