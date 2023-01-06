package com.example.youtube.repository;

import com.example.youtube.entity.VideoTagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VideoTagRepository extends CrudRepository<VideoTagEntity, Integer> {

    @Query("select  v.ownerId from VideoEntity v where  v.id=?1")
    Integer getOwnerId(String videoId);

    @Transactional
    long deleteByVideoIdAndTagId(String videoId, Integer tagId);


    List<VideoTagEntity> findByVideoId(String videoId);
}
