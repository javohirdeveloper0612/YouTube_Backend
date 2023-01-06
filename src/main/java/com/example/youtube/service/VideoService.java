package com.example.youtube.service;

import com.example.youtube.dto.profile.ProfileShortDTO;
import com.example.youtube.exp.*;
import com.example.youtube.config.security.CustomUserDetails;
import com.example.youtube.dto.attach.AttachShortDTO;
import com.example.youtube.dto.category.CategoryShortDTO;
import com.example.youtube.dto.tag.TagShortDTO;
import com.example.youtube.dto.video.VideoFullInfo;
import com.example.youtube.dto.video.VideoShortInfo;
import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelResponseDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.video.VideoCreateDTO;
import com.example.youtube.dto.video.VideoUpdateDTO;
import com.example.youtube.dto.video.tag.VideoTagInfoDTO;
import com.example.youtube.dto.video.*;
import com.example.youtube.entity.VideoEntity;
import com.example.youtube.entity.VideoWatchedEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.enums.VideoStatus;
import com.example.youtube.exp.VideoOwnerException;
import com.example.youtube.exp.VideoWatchedAlreadyExistsException;
import com.example.youtube.mapper.video.VideoShortInfoMapper;
import com.example.youtube.repository.VideoRepository;
import com.example.youtube.repository.VideoWatchedRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class VideoService {
    private final VideoRepository repository;

    private final AttachService attachService;
    private final ChannelService channelService;

    private final ResourceBundleService resourceBundleService;

    private final VideoWatchedRepository videoWatchedRepository;

    private final VideoTagService videoTagService;

    public VideoService(VideoRepository repository, AttachService attachService, ChannelService channelService, ResourceBundleService resourceBundleService, VideoWatchedRepository videoWatchedRepository, VideoTagService videoTagService) {
        this.repository = repository;
        this.attachService = attachService;
        this.channelService = channelService;
        this.resourceBundleService = resourceBundleService;
        this.videoWatchedRepository = videoWatchedRepository;
        this.videoTagService = videoTagService;
    }


    public VideoShortInfo create(VideoCreateDTO dto, Integer id, Language language) {
        VideoEntity entity = new VideoEntity();
        entity.setChannelId(dto.getChannelId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setOwnerId(id);


        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setDuration(dto.getDuration());
        entity.setStatus(VideoStatus.PRIVATE);

        repository.save(entity);


        ChannelResponseDTO channel = channelService.getById(entity.getChannelId(), language);

        VideoShortInfo responseDTO = new VideoShortInfo();
        responseDTO.setId(entity.getId());
        responseDTO.setDuration(dto.getDuration());
        responseDTO.setViewCount(entity.getViewCount());
        responseDTO.setTitle(entity.getTitle());
        responseDTO.setPublishedDate(entity.getPublishedDate());

        ChannelShortDTO channelShortDTO = new ChannelShortDTO();
        channelShortDTO.setId(channel.getId());
        channelShortDTO.setName(channel.getName());
        channelShortDTO.setPhotoUrl(attachService.getUrl(channel.getPhotoId(), language));

        PreviewAttachDTO previewAttachDTO = new PreviewAttachDTO();
        previewAttachDTO.setId(entity.getPreviewAttachId());
        previewAttachDTO.setUrl(attachService.getUrl(entity.getAttachId(), language));


        responseDTO.setChannel(channelShortDTO);
        responseDTO.setPreviewAttach(previewAttachDTO);


        return responseDTO;

    }


    public VideoShortInfo update(VideoUpdateDTO dto, Integer userId, Language language) {
        Optional<VideoEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        VideoEntity entity = optional.get();

        if (!entity.getOwnerId().equals(userId)) {
            throw new VideoOwnerException(resourceBundleService.getMessage("video.owner", language));
        }

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCategoryId(dto.getCategoryId());
        entity.setPreviewAttachId(dto.getPreviewAttachId());

        repository.save(entity);

        ChannelResponseDTO channel = channelService.getById(entity.getChannelId(), language);

        VideoShortInfo responseDTO = new VideoShortInfo();
        responseDTO.setId(entity.getId());
        responseDTO.setViewCount(entity.getViewCount());
        responseDTO.setTitle(entity.getTitle());
        responseDTO.setPublishedDate(entity.getPublishedDate());

        ChannelShortDTO channelShortDTO = new ChannelShortDTO();
        channelShortDTO.setId(channel.getId());
        channelShortDTO.setName(channel.getName());
        channelShortDTO.setPhotoUrl(attachService.getUrl(channel.getPhotoId(), language));

        PreviewAttachDTO previewAttachDTO = new PreviewAttachDTO();
        previewAttachDTO.setId(entity.getPreviewAttachId());
        previewAttachDTO.setUrl(attachService.getUrl(entity.getAttachId(), language));


        responseDTO.setChannel(channelShortDTO);
        responseDTO.setPreviewAttach(previewAttachDTO);

        return responseDTO;

    }

    public Boolean change(String id, Integer userId, Language language) {

        Optional<VideoEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }
        VideoEntity entity = optional.get();

        if (!entity.getOwnerId().equals(userId)) {
            throw new VideoOwnerException(resourceBundleService.getMessage("video.owner", language));
        }

        entity.setStatus(VideoStatus.PUBLIC);
        entity.setPublishedDate(LocalDateTime.now());

        repository.save(entity);

        return true;
    }

    public Boolean increaseViewCount(String id, Integer userId, Language language) {

        Optional<VideoEntity> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        VideoWatchedEntity videoWatched = videoWatchedRepository.findByProfileIdAndVideoId(userId, id);

        if (videoWatched != null) {
            throw new VideoWatchedAlreadyExistsException(resourceBundleService.getMessage("video.watched.exists", language));
        }

        videoWatched = new VideoWatchedEntity();

        videoWatched.setVideoId(id);
        videoWatched.setProfileId(userId);
        videoWatched.setCreatedDate(LocalDateTime.now());
        videoWatchedRepository.save(videoWatched);

        VideoEntity entity = optional.get();
        entity.setViewCount(entity.getViewCount() + 1);

        repository.save(entity);
        return true;

    }

    public Page<VideoShortInfo> getByCategoryId(Integer c_id, Integer page, Integer size, Language language) {
        Sort sort = Sort.by(Sort.Direction.DESC, "viewCount");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VideoShortInfoMapper> pageMapper = repository.getByCategoryId(c_id, pageable);

        List<VideoShortInfo> dtoList = new ArrayList<>();

        for (VideoShortInfoMapper mapper : pageMapper) {

            dtoList.add(getShortInfoDTO(mapper, language));
        }

        return new PageImpl<>(dtoList, pageable, pageMapper.getTotalElements());

    }


    public VideoShortInfo searchByTitle(String title, Language language) {

        VideoShortInfoMapper mapper = repository.searchByTitle(title);

        return getShortInfoDTO(mapper, language);

    }


    public VideoShortInfo getShortInfoDTO(VideoShortInfoMapper mapper, Language language) {

        VideoShortInfo dto = new VideoShortInfo();
        dto.setId(mapper.getId());
        dto.setDuration(mapper.getDuration());
        dto.setViewCount(mapper.getViewCount());
        dto.setTitle(mapper.getTitle());
        dto.setPublishedDate(mapper.getPublishedDate());

        ChannelShortDTO channelShortDTO = new ChannelShortDTO();
        channelShortDTO.setId(mapper.getChannel().getId());
        channelShortDTO.setName(mapper.getChannel().getName());

        channelShortDTO.setPhotoUrl(attachService.getUrl(mapper.getChannel().getPhotoId(), language));

        PreviewAttachDTO previewAttachDTO = new PreviewAttachDTO();
        previewAttachDTO.setId(mapper.getPreviewAttach().getId());

        previewAttachDTO.setUrl(attachService.getUrl(mapper.getPreviewAttach().getId(), language));


        dto.setChannel(channelShortDTO);
        dto.setPreviewAttach(previewAttachDTO);


        return dto;

    }

    public VideoShortInfo getShortInfoDTO(VideoEntity entity, Language language) {

        VideoShortInfo dto = new VideoShortInfo();
        dto.setId(entity.getId());
        dto.setDuration(entity.getDuration());
        dto.setViewCount(entity.getViewCount());
        dto.setTitle(entity.getTitle());
        dto.setPublishedDate(entity.getPublishedDate());

        ChannelShortDTO channelShortDTO = new ChannelShortDTO();
        channelShortDTO.setId(entity.getChannelId());
        channelShortDTO.setName(entity.getChannel().getName());

        channelShortDTO.setPhotoUrl(attachService.getUrl(entity.getChannel().getPhotoId(), language));

        PreviewAttachDTO previewAttachDTO = new PreviewAttachDTO();
        previewAttachDTO.setId(entity.getPreviewAttach().getId());

        previewAttachDTO.setUrl(attachService.getUrl(entity.getPreviewAttachId(), language));


        dto.setChannel(channelShortDTO);
        dto.setPreviewAttach(previewAttachDTO);


        return dto;

    }

    public Page<VideoShortInfo> getByTagId(Integer tagId, Integer page, Integer size, Language language) {


        Pageable pageable = PageRequest.of(page, size);

        Page<VideoShortInfoMapper> mapperPage = repository.getByTagId(tagId, pageable);

        List<VideoShortInfo> dtoList = new ArrayList<>();

        for (VideoShortInfoMapper mapper : mapperPage) {

            dtoList.add(getShortInfoDTO(mapper, language));
        }

        return new PageImpl<>(dtoList, pageable, mapperPage.getTotalElements());

    }

    public VideoFullInfo getFullInfoById(String videoId, CustomUserDetails user, Language language) {

        Optional<VideoEntity> optional = repository.findById(videoId);
        if (optional.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        VideoEntity entity = optional.get();

        VideoFullInfo dto = new VideoFullInfo();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());

        PreviewAttachDTO previewAttachDTO = new PreviewAttachDTO();
        previewAttachDTO.setId(entity.getPreviewAttachId());
        previewAttachDTO.setUrl(attachService.getUrl(entity.getPreviewAttachId(), language));

        dto.setPreviewAttach(previewAttachDTO);

        AttachShortDTO attachShortDTO = new AttachShortDTO();
        attachShortDTO.setId(entity.getAttachId());
        attachShortDTO.setDuration(entity.getAttach().getDuration());
        attachShortDTO.setUrl(attachService.getUrl(entity.getAttachId(), language));

        dto.setAttach(attachShortDTO);


        CategoryShortDTO categoryShortDTO = new CategoryShortDTO();
        categoryShortDTO.setId(entity.getCategoryId());
        categoryShortDTO.setName(entity.getCategory().getName());

        dto.setCategory(categoryShortDTO);


        dto.setDuration(entity.getDuration());
        dto.setType(entity.getType());
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());


        List<VideoTagInfoDTO> videoTagList = videoTagService.getByVideoId(entity.getId(), language);
        List<TagShortDTO> tagShortDTOList = new ArrayList<>();
        for (VideoTagInfoDTO videoTagInfoDTO : videoTagList) {
            tagShortDTOList.add(videoTagInfoDTO.getTagShortDTO());
        }

        dto.setTagList(tagShortDTOList);

        dto.setPublishedDate(entity.getPublishedDate());


        ChannelShortDTO channelShortDTO = new ChannelShortDTO();
        channelShortDTO.setId(entity.getChannelId());
        channelShortDTO.setName(entity.getChannel().getName());
        channelShortDTO.setPhotoUrl(attachService.getUrl(entity.getChannel().getPhotoId(), language));

        dto.setChannel(channelShortDTO);

        dto.setStatus(entity.getStatus());

        if (entity.getStatus().equals(VideoStatus.PRIVATE)) {
            if (user.getRole().equals(ProfileRole.ROLE_ADMIN) || entity.getOwnerId().equals(user.getId())) {
                return dto;
            }
        } else {
            return dto;
        }

        throw new VideoNotPublicException(resourceBundleService.getMessage("", language));

    }

    public Page<VideoSuperDTO> getList(Integer page, Integer size, Language language) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoEntity> all = repository.findAll(pageable);

        List<VideoEntity> content = all.getContent();
        List<VideoSuperDTO> dtoList = new ArrayList<>();

        for (VideoEntity entity : content) {
            VideoSuperDTO dto = new VideoSuperDTO();
            dto.setVideoShortInfo(getShortInfoDTO(entity, language));


            ProfileShortDTO profileShortDTO = new ProfileShortDTO();
            profileShortDTO.setId(entity.getOwnerId());
            profileShortDTO.setName(entity.getOwner().getName());
            profileShortDTO.setSurname(entity.getOwner().getSurname());

            dto.setOwnerDTO(profileShortDTO);

            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, all.getTotalElements());
    }
}
