package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.exp.ItemNotFoundException;
import com.example.exp.category.CategoryExistsException;
import com.example.exp.category.CategoryNotFoundException;
import com.example.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    private final ResourceBundleService resourceBundleService;

    public CategoryService(CategoryRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    public Page<CategoryDTO> getAll(int page, int size) {
        List<CategoryDTO> list =new ArrayList<>();
        Pageable pageable = PageRequest.of(page,size);

        Page<CategoryEntity> entityPage = repository.findAll(pageable);

        for (CategoryEntity entity : entityPage) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setCreatedDate(entity.getCreatedDate());
            list.add(dto);
        }


        return new PageImpl<>(list,pageable,entityPage.getTotalElements());
    }

    public CategoryDTO add(CategoryDTO dto, Language language) {

        Optional<CategoryEntity> optional = repository.findByName(dto.getName());
        if (optional.isPresent()){
            throw new CategoryExistsException(resourceBundleService.getMessage("category.exists",language));
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setCreatedDate(LocalDateTime.now());
        repository.save(entity);
        return getDTO(entity);
    }

    public CategoryDTO getDTO(CategoryEntity entity){
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public CategoryDTO edit(CategoryDTO dto, Integer id, Language language) {
        Optional<CategoryEntity> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new CategoryNotFoundException(resourceBundleService.getMessage("category.not.found",language));
        }
        CategoryEntity entity = optional.get();
        entity.setName(dto.getName());
        repository.save(entity);

        return getDTO(entity);
    }

    public String  deleteById(Integer id, Language language) {
        Optional<CategoryEntity> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new CategoryNotFoundException(resourceBundleService.getMessage("category.not.found",language));
        }
        CategoryEntity entity = optional.get();
        repository.delete(entity);
        return "Deleted";
    }

    public CategoryDTO getById(Integer id, Language language) {
        Optional<CategoryEntity> optional = repository.findById(id);

        if (optional.isEmpty()){
            throw new CategoryNotFoundException(resourceBundleService.getMessage("category.not.found",language));
        }

        return getDTO(optional.get());
    }
}
