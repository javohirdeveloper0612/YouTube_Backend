package com.example.youtube.repository;

import com.example.youtube.entity.VideoEntity;
import com.example.youtube.entity.VideoLikeEntity;
import com.example.youtube.enums.LikeType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity, Integer>,
        PagingAndSortingRepository<VideoLikeEntity, Integer> {

    List<VideoLikeEntity> findAllByProfileIdAndLikeTypeOrderByCreatedDate(Integer profileId, LikeType likeType);


    Optional<VideoLikeEntity> findByProfileIdAndVideoId(Integer profileId, String videoId);
}
