package com.example.youtube.controller;

import com.example.youtube.dto.category.CategoryDTO;
import com.example.youtube.entity.CategoryEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "thes is Control Category")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @PreAuthorize("permitAll()")
    @GetMapping
    public HttpEntity<?> getAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "1") int size) {

        Page<CategoryDTO> allStudentFromDb = service.getAllFromDb(page, size);


        return ResponseEntity.ok(allStudentFromDb);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public HttpEntity<?> add(@Valid @RequestBody CategoryDTO dto,
                             @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        String result = service.add(dto, language);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@Valid @RequestBody CategoryDTO dto, @PathVariable Integer id,
                              @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        CategoryDTO result = service.edit(dto, id, language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id,
                                 @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        CategoryEntity continentById = service.getById(id, language);
        return ResponseEntity.ok(continentById);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable Integer id,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "RU") Language language) {
        Boolean result = service.deleteById(id, language);
        return ResponseEntity.ok(result);
    }
}
