package com.example.youtube.repository;

import com.example.youtube.entity.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity, Integer> {
    @Query(value = "select count(*) from playlist where channel_id=?1", nativeQuery = true)
    Integer findDistinctFirstByOrderNum(String channelId);

    Page<PlaylistEntity> findAll(Pageable pageable);

    @Query("from PlaylistEntity where channelId=?1 order by orderNum desc")
    List<PlaylistEntity> findByChannelIdOrderByOrderNumDesc(String channelId);

    Optional<PlaylistEntity> findById(Integer id);

    List<PlaylistEntity> findByOwnerId(Integer ownerId);
}
