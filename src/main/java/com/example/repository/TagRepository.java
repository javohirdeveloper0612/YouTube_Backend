package com.example.repository;

import com.example.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<TagEntity,Integer> {
boolean existsByName(String name);
}
