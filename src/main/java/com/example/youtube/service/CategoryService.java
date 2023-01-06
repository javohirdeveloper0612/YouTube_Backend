package com.example.youtube.service;

import com.example.youtube.dto.category.CategoryDTO;
import com.example.youtube.entity.CategoryEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.exp.CategoryAlreadyExistsException;
import com.example.youtube.exp.CategoryNotFoundException;
import com.example.youtube.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final ResourceBundleService resourceBundleService;

    public CategoryService(CategoryRepository repo, ResourceBundleService resourceBundleService) {
        this.repo = repo;
        this.resourceBundleService = resourceBundleService;
    }

    public Page<CategoryDTO> getAllFromDb(int page, int size) {

        List<CategoryDTO> list = new LinkedList<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<CategoryEntity> page1 = repo.findAll(pageable);


        for (CategoryEntity category : page1.getContent()) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setCreatedDate(category.getCreatedDate());
            list.add(dto);
        }


        return new PageImpl<>(list, pageable, page1.getTotalElements());
    }


    public CategoryEntity getById(Integer id,Language language) {
        Optional<CategoryEntity> optional = repo.findById(id);
        if (optional.isEmpty()) {
            throw new CategoryNotFoundException(resourceBundleService.getMessage("category.not.found",language));
        }
        return optional.get();
    }

    public Boolean deleteById(Integer id,Language language) {
        getById(id, language);

        repo.deleteById(id);
        return true;
    }

    public String add(CategoryDTO dto, Language language) {

        CategoryEntity entity = repo.findByName(dto.getName());

        if (entity != null) {
            throw new CategoryAlreadyExistsException(resourceBundleService.getMessage("category.alRead",language));
        }

        CategoryEntity entity1 = new CategoryEntity();
        entity1.setName(dto.getName());
        entity1.setCreatedDate(LocalDateTime.now());
        repo.save(entity1);
        return "add";
    }

    public CategoryDTO edit(CategoryDTO dto, Integer id,Language language) {
        Optional<CategoryEntity> optional = repo.findById(id);
        if (optional.isEmpty()) {
            throw new CategoryNotFoundException(resourceBundleService.getMessage("category.not.found",language));
        }


        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());


        repo.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}

