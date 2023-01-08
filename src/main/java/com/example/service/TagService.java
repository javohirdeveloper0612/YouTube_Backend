package com.example.service;

import com.example.dto.TagCreateDTO;
import com.example.dto.TagDTO;
import com.example.dto.TagResponseDTO;
import com.example.entity.TagEntity;
import com.example.enums.Language;
import com.example.exp.tag.TagAlreadyExistsException;
import com.example.exp.tag.TagNotFoundException;
import com.example.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository repository;

    public TagService(TagRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
    }

    private final ResourceBundleService resourceBundleService;

    public TagResponseDTO create(TagCreateDTO dto, Language language) {

        boolean result = repository.existsByName(dto.getName());
        if (result){
            throw new TagAlreadyExistsException(resourceBundleService.getMessage("tag.exists",language));
        }

        TagEntity tagEntity = new TagEntity();
        if (!dto.getName().startsWith("#")) {
            tagEntity.setName("#" + dto.getName());
        } else {
            tagEntity.setName(dto.getName());
        }


        tagEntity.setCreatedDate(LocalDateTime.now());
        repository.save(tagEntity);

        TagResponseDTO response = new TagResponseDTO();
        response.setId(tagEntity.getId());
        response.setName(tagEntity.getName());
        response.setCreatedDate(LocalDateTime.now());

        return response;

    }

    public String updateById(Integer id, TagCreateDTO dto, Language language) {
        Optional<TagEntity> byId = repository.findById(id);

        if (byId.isEmpty()) {

            throw new TagNotFoundException(resourceBundleService.getMessage("tag.not.found", language));
        }

        TagEntity tagEntity = byId.get();
        if (!dto.getName().startsWith("#")) {
            tagEntity.setName("#" + dto.getName());
        } else {
            tagEntity.setName(dto.getName());
        }

        repository.save(tagEntity);
        return "updated";
    }

    public String deleteById(Integer id, Language language) {
        Optional<TagEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new TagNotFoundException(resourceBundleService.getMessage("tag.not.found", language));
        }

        repository.delete(optional.get());
        return "Deleted";
    }

    public List<TagResponseDTO> tagList() {
        Iterable<TagEntity> all = repository.findAll();

        List<TagResponseDTO> result = new ArrayList<>();
        for (TagEntity tagEntity : all) {
            TagResponseDTO response = new TagResponseDTO();
            response.setId(tagEntity.getId());
            response.setName(tagEntity.getName());
            response.setCreatedDate(tagEntity.getCreatedDate());
            result.add(response);
        }

        return result;

    }
}
