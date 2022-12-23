package com.example.repository;

import com.example.entity.ChannelEntity;
import com.example.mappers.ChannelDTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<ChannelEntity,String> {

    @Query("select new com.example.mappers.ChannelDTOMapper(c.name, c.photo , c.description , c.banner ) from ChannelEntity c ")
    Page<ChannelDTOMapper> getList(Pageable pageable);

}
