package com.example.youtube.repository;

import com.example.youtube.entity.VideoEntity;
import com.example.youtube.mapper.video.VideoShortInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface VideoRepository extends CrudRepository<VideoEntity, String>, PagingAndSortingRepository<VideoEntity, String> {


    @Query("SELECT new com.example.youtube.mapper.video.VideoShortInfoMapper(v.id,v.title,v.previewAttach,v.publishedDate,v.channel,v.viewCount,v.duration) from VideoEntity v where v.categoryId=?1 and v.status='PUBLIC'")
    Page<VideoShortInfoMapper> getByCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT new com.example.youtube.mapper.video.VideoShortInfoMapper(v.id,v.title,v.previewAttach,v.publishedDate,v.channel,v.viewCount,v.duration) from VideoEntity v where v.title=?1 and v.status='PUBLIC'")
    VideoShortInfoMapper searchByTitle(String title);


    @Query("SELECT new com.example.youtube.mapper.video.VideoShortInfoMapper(v.id,v.title,v.previewAttach,v.publishedDate,v.channel,v.viewCount,v.duration) from  VideoEntity  v  " +
            " inner join VideoTagEntity vt on vt.tagId=?1 where  v.status='PUBLIC' order by v.viewCount desc ")
    Page<VideoShortInfoMapper> getByTagId(Integer tagId,Pageable pageable);

}
