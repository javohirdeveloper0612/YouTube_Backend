package com.example.youtube.service;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import com.example.youtube.dto.playlist.PlaylistResponseDTO;
import com.example.youtube.dto.playlist.PlaylistSmallInfoDTO;
import com.example.youtube.dto.playlistVideo.*;
import com.example.youtube.dto.video.VideoShortDTO;
import com.example.youtube.entity.*;
import com.example.youtube.enums.Language;
import com.example.youtube.exp.VideoNotFoundException;
import com.example.youtube.exp.playlist.PlaylistNoAccessException;
import com.example.youtube.exp.playlist.PlaylistNotFoundException;
import com.example.youtube.repository.PlaylistRepository;
import com.example.youtube.repository.PlaylistVideoRepository;
import com.example.youtube.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistVideoService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ResourceBundleService resourceBundleService;

    @Value("${attach.download.url}")
    private String attachDownloadUrl;


    public PlaylistVideoResponseDTO create(PlaylistVideoCreateDTO dto, Integer userId, Language language) {
        Optional<VideoEntity> videoExists = videoRepository.findById(dto.getVideoId());
        if (videoExists.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        Optional<PlaylistEntity> byPlaylistId = playlistRepository.findById(dto.getPlaylistId());
        if (byPlaylistId.isEmpty()) {
            throw new PlaylistNotFoundException(resourceBundleService.getMessage("playlist.not.found", language));
        }

        PlaylistEntity playlistEntity = byPlaylistId.get();

        if (!playlistEntity.getOwnerId().equals(userId)) {
            throw new PlaylistNoAccessException(resourceBundleService.getMessage("playlist.no.access", language));
        }


        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setVideoId(dto.getVideoId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);

        PlaylistVideoResponseDTO response = new PlaylistVideoResponseDTO();
        response.setId(entity.getId());
        response.setPlayListId(entity.getPlaylistId());
        response.setVideoId(entity.getVideoId());
        response.setCreatedDate(entity.getCreatedDate());
        response.setOrderNum(entity.getOrderNum());
        return response;

    }

    public Boolean update(PlaylistVideoUpdateDTO dto, Integer userId, Language language) {
        Optional<VideoEntity> byId = videoRepository.findById(dto.getVideoId());
        if (byId.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        Optional<PlaylistVideoEntity> byVideoIdAndPlaylistId = playlistVideoRepository.findByVideoIdAndPlaylistId(dto.getVideoId(), dto.getPlaylistId());
        if (byVideoIdAndPlaylistId.isEmpty()) {
            throw new PlaylistNotFoundException(resourceBundleService.getMessage("playlist.not.found", language));
        }

        PlaylistVideoEntity entity = byVideoIdAndPlaylistId.get();
        if (!entity.getVideo().getOwnerId().equals(userId)) {
            throw new PlaylistNoAccessException(resourceBundleService.getMessage("playlist.no.access", language));
        }

        entity.setOrderNum(dto.getOrderNum());
        playlistVideoRepository.save(entity);
        return true;
    }

    public Boolean delete(PlaylistVideoDeleteDTO dto, Integer userId, Language language) {
        Optional<VideoEntity> byId = videoRepository.findById(dto.getVideoId());
        if (byId.isEmpty()) {
            throw new VideoNotFoundException(resourceBundleService.getMessage("video.not.found", language));
        }

        Optional<PlaylistVideoEntity> byVideoIdAndPlaylistId = playlistVideoRepository.findByVideoIdAndPlaylistId(dto.getVideoId(), dto.getPlaylistId());
        if (byVideoIdAndPlaylistId.isEmpty()) {
            throw new PlaylistNotFoundException(resourceBundleService.getMessage("playlist.not.found", language));
        }

        PlaylistVideoEntity entity = byVideoIdAndPlaylistId.get();
        if (!entity.getVideo().getOwnerId().equals(userId)) {
            throw new PlaylistNoAccessException(resourceBundleService.getMessage("playlist.no.access", language));
        }

        playlistVideoRepository.delete(entity);
        return true;
    }

    public List<PlaylistVideoInfoDTO> getPlaylistVideo(Integer id, Language language) {
        List<PlaylistVideoEntity> byPlaylistId = playlistVideoRepository.findByPlaylistId(id);

        List<PlaylistVideoInfoDTO> result = new LinkedList<>();
        for (PlaylistVideoEntity entity : byPlaylistId) {
            result.add(toPlaylistVideoInfo(entity));
        }

        return result;
    }

    private PlaylistVideoInfoDTO toPlaylistVideoInfo(PlaylistVideoEntity entity) {
        PlaylistVideoInfoDTO dto = new PlaylistVideoInfoDTO();
        dto.setPlaylistId(entity.getId());
        dto.setVideo(toShortVideo(entity.getVideo()));

        return dto;
    }

    private VideoShortDTO toShortVideo(VideoEntity video) {
        VideoShortDTO dto = new VideoShortDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDuration(video.getDuration());
        dto.setChannel(toChannelShort(video.getChannel()));
        dto.setPreviewAttach(toPreviewAttach(video.getPreviewAttach()));

        return dto;
    }

    private PreviewAttachDTO toPreviewAttach(AttachEntity entity) {
        PreviewAttachDTO dto = new PreviewAttachDTO();
        if (entity != null) {
            dto.setId(entity.getId());
            dto.setUrl(attachDownloadUrl + entity.getId() + "." + entity.getType());

        }
        return dto;
    }

    private ChannelShortInfoDTO toChannelShort(ChannelEntity channel) {
        ChannelShortInfoDTO dto = new ChannelShortInfoDTO();
        if (channel != null) {
            dto.setId(channel.getId());
            dto.setName(channel.getName());
        }

        return dto;
    }
}
