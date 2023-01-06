package com.example.youtube.repository;

import com.example.youtube.entity.ChannelEntity;
import com.example.youtube.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends CrudRepository<ChannelEntity, String> {
    Optional<ChannelEntity> findById(String s);
    Page<ChannelEntity> findAll(Pageable pageable);

    Page<ChannelEntity> findAllByProfileId(Integer profileId, Pageable pageable);
}
