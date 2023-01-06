package com.example.repository;

import com.example.entity.AttachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AttachRepository extends JpaRepository<AttachEntity,String> {

}
