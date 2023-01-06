package com.example.youtube.repository;

import com.example.youtube.entity.SubscriptionEntity;
import com.example.youtube.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Integer> {



    Optional<SubscriptionEntity> findByChannelId(Integer id);

    List<SubscriptionEntity> findByProfileId(Integer id);
}
