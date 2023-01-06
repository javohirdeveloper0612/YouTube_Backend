package com.example.youtube.repository;

import com.example.youtube.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity, Integer> {


    Page<ReportEntity> findAll(Pageable pageable);

 

    List<ReportEntity> findByProfileId(Integer profileId);

}
