package com.example.youtube.service;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import com.example.youtube.dto.playlist.*;
import com.example.youtube.dto.profile.ProfileInfoDTO;
import com.example.youtube.dto.video.VideoSmallInfoDTO;
import com.example.youtube.entity.*;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.PlaylistStatus;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.exp.channel.ChannelAccessDeniedException;
import com.example.youtube.exp.channel.ChannelNotExistsException;
import com.example.youtube.exp.playlist.PlaylistNotFoundException;
import com.example.youtube.exp.playlist.PlaylistNoAccessException;
import com.example.youtube.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlaylistService {
    @Value("${attach.download.url}")
    private String attachDownloadUrl;

    private final PlaylistRepository playlistRepository;
    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceBundleService;
    private final ChannelRepository channelRepository;
    private final PlaylistVideoRepository playlistVideoRepository;
    @Autowired
    private VideoWatchedRepository videoWatchedRepository;


    public PlaylistService(PlaylistRepository playlistRepository, ProfileRepository profileRepository, ResourceBundleService resourceBundleService, ChannelRepository channelRepository, PlaylistVideoRepository playlistVideoRepository) {
        this.playlistRepository = playlistRepository;
        this.profileRepository = profileRepository;
        this.resourceBundleService = resourceBundleService;
        this.channelRepository = channelRepository;
        this.playlistVideoRepository = playlistVideoRepository;
    }


    public PlaylistResponseDTO create(PlaylistCreateDTO dto, Integer userId, Language language) {
        if (channelRepository.findById(dto.getChannelId()).isEmpty()) {
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        PlaylistEntity playlistEntity = new PlaylistEntity();
        playlistEntity.setChannelId(dto.getChannelId());
        playlistEntity.setName(dto.getName());
        playlistEntity.setDescription(dto.getDescription());
        playlistEntity.setStatus(dto.getStatus());
        Integer lastOrder = playlistRepository.findDistinctFirstByOrderNum(dto.getChannelId());
        lastOrder++;
        playlistEntity.setOrderNum(lastOrder);
        playlistEntity.setOwnerId(userId);

        playlistRepository.save(playlistEntity);

        PlaylistResponseDTO response = new PlaylistResponseDTO();
        response.setId(playlistEntity.getId());
        response.setChannelId(playlistEntity.getChannelId());
        response.setName(playlistEntity.getName());
        response.setDescription(playlistEntity.getDescription());
        response.setStatus(playlistEntity.getStatus());
        response.setOrderNum(playlistEntity.getOrderNum());
        return response;
    }

    public Boolean update(Integer id, PlaylistUpdateDTO dto, Integer profileId, Language language) {
        Optional<PlaylistEntity> byId = playlistRepository.findById(id);

        if (byId.isEmpty()) {
            log.warn("Playlist not found: {} ", id);
            throw new PlaylistNotFoundException(resourceBundleService.getMessage("playlist.not.found", language));
        }

        PlaylistEntity playlistEntity = byId.get();

        ChannelEntity channelEntity = playlistEntity.getChannel();

        if (!channelEntity.getProfileId().equals(profileId)) {
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        playlistEntity.setName(dto.getName());
        playlistEntity.setDescription(dto.getDescription());
        playlistRepository.save(playlistEntity);
        return true;
    }


    public Boolean changeStatus(Integer id, Integer profileId, Language language) {
        Optional<PlaylistEntity> byId = playlistRepository.findById(id);
        if (byId.isEmpty()) { //checking channel is exists
            log.warn("Channel not found: {} ", id);
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        PlaylistEntity playlistEntity = byId.get();

        ChannelEntity channelEntity = playlistEntity.getChannel();

        if (!channelEntity.getProfileId().equals(profileId)) {
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        if (!channelEntity.getProfileId().equals(profileId)) { //checking channel is owned by user
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        if (playlistEntity.getStatus() == PlaylistStatus.PUBLIC) {
            log.info("{}, Playlist status updated to PRIVATE", id);
            playlistEntity.setStatus(PlaylistStatus.PRIVATE);
            playlistRepository.save(playlistEntity);

        } else {
            log.info("{}, Playlist status updated to PUBLIC", id);
            playlistEntity.setStatus(PlaylistStatus.PUBLIC);
            playlistRepository.save(playlistEntity);
        }

        return true;
    }

    public Boolean deleteById(Integer id, Integer profileId, Language language) {
        Optional<PlaylistEntity> byId = playlistRepository.findById(id);

        if (byId.isEmpty()) {
            log.warn("Playlist not found: {}", id);
            throw new PlaylistNotFoundException(resourceBundleService.getMessage("playlist.not.found", language));
        }

        PlaylistEntity playlistEntity = byId.get();
        if (playlistEntity.getChannel().getProfileId() != profileId) {
            ProfileEntity profile = profileRepository.findById(id).get();

            if (profile.getRole() != ProfileRole.ROLE_ADMIN) {
                log.warn("Access denied: {} from ", id, profileId);
                throw new PlaylistNoAccessException(resourceBundleService.getMessage("playlist.no.access", language));
            }

            playlistRepository.delete(playlistEntity);
            return true;
        }

        playlistRepository.delete(playlistEntity);
        return true;
    }


    public Page<PlayListInfoDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PlaylistEntity> pageObj = playlistRepository.findAll(pageable);

        List<PlaylistEntity> content = pageObj.getContent();

        List<PlayListInfoDTO> dtoList = new ArrayList<>();

        for (PlaylistEntity playlistEntity : content) {
            dtoList.add(getPlaylistInfo(playlistEntity));
        }

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public List<PlayListInfoDTO> getListByUserId(Integer id) {
        List<PlaylistEntity> byId = playlistRepository.findByOwnerId(id);

        List<PlayListInfoDTO> response = new ArrayList<>();
        for (PlaylistEntity playlistEntity : byId) {
            response.add(getPlaylistInfo(playlistEntity));
        }

        return response;
    }

    public List<PlayListShortInfoDTO> getShortListByUserId(Integer id) {
        List<PlaylistEntity> byId = playlistRepository.findByOwnerId(id);

        List<PlayListShortInfoDTO> response = new ArrayList<>();
        for (PlaylistEntity playlistEntity : byId) {
            response.add(getPlaylistShortInfo(playlistEntity));
        }

        return response;
    }

    public List<PlayListInfoDTO> getListByChannelId(String channelId) {
        List<PlaylistEntity> byId = playlistRepository.findByChannelIdOrderByOrderNumDesc(channelId);

        List<PlayListInfoDTO> response = new ArrayList<>();
        for (PlaylistEntity playlistEntity : byId) {
            response.add(getPlaylistInfo(playlistEntity));
        }

        return response;
    }

//    public List<PlayListShortInfoDTO> getOwnerShortPlaylist(Integer userId) {
//        Optional<ChannelEntity> channelByProfileId = channelRepository.findAllByProfileId(userId);
//        channelByProfileId.orElseThrow(() -> {
//            throw new RuntimeException();
//        });
//
//        List<PlaylistEntity> byId = playlistRepository.findByChannelIdOrderByOrderNumDesc(channelByProfileId.get().getId());
//
//        List<PlayListShortInfoDTO> response = new ArrayList<>();
//        for (PlaylistEntity playlistEntity : byId) {
//            response.add(getPlaylistShortInfo(playlistEntity));
//        }
//
//        return response;
//    }

    public List<PlayListShortInfoDTO> getPlaylistByChannelId(String channelId) {
        List<PlaylistEntity> byId = playlistRepository.findByChannelIdOrderByOrderNumDesc(channelId);
        List<PlayListShortInfoDTO> response = new ArrayList<>();
        for (PlaylistEntity playlistEntity : byId) {
            response.add(getPlaylistShortInfo(playlistEntity));
        }

        return response;
    }

    public PlaylistSmallInfoDTO getByPlaylistId(Integer playlistId) {
        Optional<PlaylistEntity> byId = playlistRepository.findById(playlistId);
        byId.orElseThrow(() -> {
            throw new RuntimeException("Playlist is not found");
        });

        PlaylistSmallInfoDTO response = toProfileSmallInfo(byId.get());
        return response;
    }

    private PlaylistSmallInfoDTO toProfileSmallInfo(PlaylistEntity entity) {
        PlaylistSmallInfoDTO dto = new PlaylistSmallInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        List<VideoSmallInfoDTO> videosByPlaylist = getVideosByPlaylist(entity);
        dto.setVideoCount(videosByPlaylist.size());

        Integer total = 0;
        for (VideoSmallInfoDTO item : videosByPlaylist) {
            total += videoWatchedRepository.findByVideoId(item.getId());
        }
        dto.setVideoTotalCount(total);
        return dto;
    }

    private PlayListShortInfoDTO getPlaylistShortInfo(PlaylistEntity entity) {
        PlayListShortInfoDTO dto = new PlayListShortInfoDTO();
        dto.setId(entity.getId());
        dto.setChannel(getChannelShortInfo(entity.getChannel()));
        List<VideoSmallInfoDTO> videosByPlaylist = getVideosByPlaylist(entity);
        dto.setVideoCount(videosByPlaylist.size());
        dto.setVideoList(videosByPlaylist);
        dto.setStatus(entity.getStatus());
        dto.setOrderNum(entity.getOrderNum());

        return dto;
    }

    private List<VideoSmallInfoDTO> getVideosByPlaylist(PlaylistEntity entity) {
        List<VideoSmallInfoDTO> videosByPlaylist = new LinkedList<>();
        List<PlaylistVideoEntity> byPlaylistId = playlistVideoRepository.findByPlaylistId(entity.getId());
        for (PlaylistVideoEntity playlistVideo : byPlaylistId) {
            videosByPlaylist.add(toVideoSmallInfo(playlistVideo.getVideo()));
        }

        return videosByPlaylist;
    }


    private VideoSmallInfoDTO toVideoSmallInfo(VideoEntity entity) {
        VideoSmallInfoDTO dto = new VideoSmallInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDuration(entity.getDuration());

        return dto;
    }

    private ChannelShortInfoDTO getChannelShortInfo(ChannelEntity entity) {
        ChannelShortInfoDTO dto = new ChannelShortInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    private PlayListInfoDTO getPlaylistInfo(PlaylistEntity entity) {
        PlayListInfoDTO dto = new PlayListInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setOrderNum(entity.getOrderNum());
        dto.setChannel(toChannelInfo(entity.getChannel()));
        dto.setProfile(toProfileInfo(entity.getOwner()));

        return dto;
    }

    private ProfileInfoDTO toProfileInfo(ProfileEntity entity) {
        ProfileInfoDTO dto = new ProfileInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhoto(getPreviewAttach(entity.getPhoto()));

        return dto;
    }

    private PreviewAttachDTO getPreviewAttach(AttachEntity entity) {
        PreviewAttachDTO dto = new PreviewAttachDTO();
        if (entity != null) {
            dto.setId(entity.getId());
            dto.setUrl(attachDownloadUrl + entity.getId() + "." + entity.getType());
        }

        return dto;
    }

    private ChannelShortDTO toChannelInfo(ChannelEntity entity) {
        ChannelShortDTO dto = new ChannelShortDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhotoUrl(attachDownloadUrl + entity.getPhotoId() + "." + entity.getPhoto().getType());

        return dto;
    }
}