package com.example.repository;

import com.example.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity,Integer> {


    Page<CategoryEntity> findAll(Pageable pageable);

    Optional<CategoryEntity> findByName(String name);
}
