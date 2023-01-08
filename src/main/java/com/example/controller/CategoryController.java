package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.enums.Language;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name = "Category",description = "this is category controller")
public class  CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "addCategory",description = "this method add new category only admin")
    @PostMapping("/add")
    public HttpEntity<?> add(@Valid @RequestBody CategoryDTO dto ,
                             @RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
        CategoryDTO result= service.add(dto,language);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/get_all")
    public HttpEntity<?> getAll (@RequestParam(name = "page",defaultValue = "1") int page,
                                 @RequestParam(name = "size",defaultValue = "1") int size){
        Page<CategoryDTO> categoryDTOS= service.getAll(page,size);

        return ResponseEntity.ok(categoryDTOS);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "edit" ,description = "this method edit category")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody CategoryDTO dto,
                                  @PathVariable Integer id,
                                  @RequestHeader(value = "Accept-Language",defaultValue = "UZ") Language language){
        CategoryDTO result = service.edit(dto,id,language);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete",description = "this method for delete category by id (only ADMIN)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id")Integer id,
                                        @RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
        String result = service.deleteById(id, language);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "getById",description = "this method get Category ById (only ADMIN)")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable("id")Integer id,
                                     @RequestHeader(value = "Accept-Language",defaultValue = "UZ")Language language){
        CategoryDTO result = service.getById(id,language);

        return ResponseEntity.ok(result);
    }


}
