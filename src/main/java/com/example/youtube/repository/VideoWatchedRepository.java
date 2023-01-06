package com.example.youtube.repository;

import com.example.youtube.entity.VideoWatchedEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoWatchedRepository extends CrudRepository<VideoWatchedEntity, String> {

    VideoWatchedEntity findByProfileIdAndVideoId(Integer profileId, String videoId);

    @Query(value = "select count(*) from video_watched where video_id = ?1", nativeQuery = true)
    Integer findByVideoId(String videoId);
}
