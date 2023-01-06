package com.example.youtube.service;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import com.example.youtube.dto.video.VideoCreateDTO;
import com.example.youtube.dto.video.VideoShortDTO;
import com.example.youtube.dto.videoLike.VideoLikeCreateDTO;
import com.example.youtube.dto.videoLike.VideoLikeInfo;
import com.example.youtube.dto.videoLike.VideoLikeResponseDTO;
import com.example.youtube.entity.*;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.LikeType;
import com.example.youtube.exp.channel.ChannelNotExistsException;
import com.example.youtube.exp.tag.TagNotFound;
import com.example.youtube.exp.videoLike.AlreadyLikedException;
import com.example.youtube.repository.VideoLikeRepository;
import com.example.youtube.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoLikeService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoLikeRepository videoLikeRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    @Value("${attach.download.url}")
    private String attachDownloadUrl;

    public VideoLikeResponseDTO create(VideoLikeCreateDTO dto, Integer profileId, Language language) {
        Optional<VideoLikeEntity> isLiked = videoLikeRepository.findByProfileIdAndVideoId(profileId, dto.getVideoId());
        if (isLiked.isPresent()) {
            log.warn("Already liked: {} ", profileId);
            throw new AlreadyLikedException(resourceBundleService.getMessage("already.liked", language));
        }

        Optional<VideoEntity> isExists = videoRepository.findById(dto.getVideoId());
        if (isExists.isEmpty()) {
            log.warn("Video not found found while {} liking a video: {} ", profileId, dto.getVideoId());
            throw new ChannelNotExistsException(resourceBundleService.getMessage("video.not.found", language));
        }

        VideoLikeEntity videoLikeEntity = new VideoLikeEntity();
        videoLikeEntity.setVideoId(dto.getVideoId());
        videoLikeEntity.setLikeType(dto.getLikeType());
        videoLikeEntity.setCreatedDate(LocalDateTime.now());
        videoLikeEntity.setProfileId(profileId);
        videoLikeRepository.save(videoLikeEntity);

        VideoLikeResponseDTO response = new VideoLikeResponseDTO();
        response.setId(videoLikeEntity.getId());
        response.setVideoId(videoLikeEntity.getVideoId());
        response.setLikeType(videoLikeEntity.getLikeType());
        response.setCreatedDate(videoLikeEntity.getCreatedDate());
        response.setProfileId(videoLikeEntity.getProfileId());
        System.out.println(videoLikeEntity.getVideo());
        return response;
    }


    public Boolean deleteById(Integer id, Integer profileId, Language language) {
        Optional<VideoLikeEntity> byId = videoLikeRepository.findById(id);

        if (byId.isEmpty()) {
            log.warn("This user is deleting Like: {} but like not found: {}", profileId, id);
            throw new TagNotFound(resourceBundleService.getMessage("like.not.found", language));
        }

        if (byId.get().getProfileId() != profileId) {
            log.warn("This user is deleting Like: {} but user is not owner: {}", id);
            throw new TagNotFound(resourceBundleService.getMessage("like.not.found", language));
        }

        videoLikeRepository.delete(byId.get());
        return true;
    }

    public List<VideoLikeInfo> getLikedVideos(Integer profileId) {
        List<VideoLikeEntity> liked = videoLikeRepository.findAllByProfileIdAndLikeTypeOrderByCreatedDate(profileId, LikeType.LIKE);

        List<VideoLikeInfo> result = new ArrayList<>();
        for (VideoLikeEntity videoLikeEntity : liked) {
            result.add(getVideoLikeInfo(videoLikeEntity));
        }

        return result;
    }


    private VideoLikeInfo getVideoLikeInfo(VideoLikeEntity entity) {
        VideoLikeInfo dto = new VideoLikeInfo();
        dto.setId(entity.getId());
        dto.setVideoShortInfo(getVideoShortInfo(entity.getVideo()));

        return dto;
    }

    private VideoShortDTO getVideoShortInfo(VideoEntity entity) {
        VideoShortDTO dto = new VideoShortDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setChannel(getChannelShortInfo(entity.getChannel()));
        dto.setDuration(entity.getAttach().getDuration());
        dto.setPreviewAttach(getPreviewAttachInfo(entity.getPreviewAttach()));

        return dto;
    }

    private PreviewAttachDTO getPreviewAttachInfo(AttachEntity entity) {
        PreviewAttachDTO dto = new PreviewAttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(attachDownloadUrl + entity.getId() + "." + entity.getType());

        return dto;
    }

    private ChannelShortInfoDTO getChannelShortInfo(ChannelEntity entity) {
        ChannelShortInfoDTO dto = new ChannelShortInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}
