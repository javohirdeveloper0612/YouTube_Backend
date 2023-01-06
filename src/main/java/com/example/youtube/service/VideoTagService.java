package com.example.youtube.service;


import com.example.youtube.dto.video.tag.VideoTagDTO;
import com.example.youtube.dto.video.tag.VideoTagInfoDTO;
import com.example.youtube.dto.video.tag.VideoTagResponseDTO;
import com.example.youtube.dto.tag.TagShortDTO;
import com.example.youtube.entity.VideoTagEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.exp.VideoOwnerException;
import com.example.youtube.repository.VideoTagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoTagService {
    private final VideoTagRepository repository;

    private final ResourceBundleService resourceBundleService;

    public VideoTagService(VideoTagRepository repository, ResourceBundleService resourceBundleService) {
        this.repository = repository;

        this.resourceBundleService = resourceBundleService;
    }

    public VideoTagResponseDTO add(VideoTagDTO dto, Integer id, Language language) {

        Integer ownerId = repository.getOwnerId(dto.getVideoId());
        if (!id.equals(ownerId)) {
            throw new VideoOwnerException(resourceBundleService.getMessage("", language));
        }

        VideoTagEntity entity = new VideoTagEntity();
        entity.setTagId(dto.getTagId());
        entity.setVideoId(dto.getVideoId());

        repository.save(entity);
        VideoTagResponseDTO responseDTO = new VideoTagResponseDTO();
        responseDTO.setTagId(dto.getTagId());
        responseDTO.setVideoId(dto.getVideoId());
        responseDTO.setCreatedDate(entity.getCreatedDate());

        return responseDTO;

    }

    public Boolean delete(VideoTagDTO dto, Integer id, Language language) {

        Integer ownerId = repository.getOwnerId(dto.getVideoId());
        if (!id.equals(ownerId)) {
            throw new VideoOwnerException(resourceBundleService.getMessage("", language));
        }


        repository.deleteByVideoIdAndTagId(dto.getVideoId(), dto.getTagId());

        return true;
    }

    public List<VideoTagInfoDTO> getByVideoId(String videoId, Language language) {

        List<VideoTagEntity> entityList = repository.findByVideoId(videoId);

        List<VideoTagInfoDTO> dtoList = new ArrayList<>();

        for (VideoTagEntity entity : entityList) {
            VideoTagInfoDTO dto = new VideoTagInfoDTO();
            dto.setId(entity.getId());
            dto.setVideoId(entity.getVideoId());

            TagShortDTO tagShortDTO = new TagShortDTO();
            tagShortDTO.setId(entity.getTag().getId());
            tagShortDTO.setName(entity.getTag().getName());

            dto.setTagShortDTO(tagShortDTO);
            dto.setCreatedDate(entity.getCreatedDate());

            dtoList.add(dto);
        }

        return dtoList;
    }
}
