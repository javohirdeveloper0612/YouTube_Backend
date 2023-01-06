package com.example.youtube.repository;

import com.example.youtube.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer>, PagingAndSortingRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAll(Pageable pageable);

    List<CommentEntity> findByProfileId(Integer profileId);

    List<CommentEntity> findByVideoId(String videoId);
}
