package com.example.youtube.service;


import com.example.youtube.dto.attach.AttachResponseDTO;
import com.example.youtube.dto.channel.ChannelCreateDTO;
import com.example.youtube.dto.channel.ChannelResponseDTO;
import com.example.youtube.dto.channel.ChannelUpdatePropertiesDTO;
import com.example.youtube.entity.ChannelEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.enums.ChannelStatus;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.ProfileRole;
import com.example.youtube.exp.FileTypeIncorrectException;
import com.example.youtube.exp.channel.ChannelAccessDeniedException;
import com.example.youtube.exp.channel.ChannelNotExistsException;
import com.example.youtube.repository.ChannelRepository;
import com.example.youtube.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    private final ResourceBundleService resourceBundleService;

    private final ProfileRepository profileRepository;

    private final AttachService attachService;

    public ChannelService(ChannelRepository channelRepository, ResourceBundleService resourceBundleService, ProfileRepository profileRepository, AttachService attachService) {
        this.channelRepository = channelRepository;
        this.resourceBundleService = resourceBundleService;
        this.profileRepository = profileRepository;
        this.attachService = attachService;
    }

    public ChannelResponseDTO create(ChannelCreateDTO dto, Integer profileId) {
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setName(dto.getName());
        channelEntity.setPhotoId(dto.getPhotoId());
        channelEntity.setDescription(dto.getDescription());
        channelEntity.setBannerId(dto.getBannerId());
        channelEntity.setProfileId(profileId);
        channelEntity.setStatus(ChannelStatus.ACTIVE);
        channelRepository.save(channelEntity);

        return toResponseDTO(channelEntity);
    }

    public Boolean update(String id, ChannelUpdatePropertiesDTO dto, Integer profileId, Language language) {
        Optional<ChannelEntity> byId = channelRepository.findById(id);
        if (byId.isEmpty()) {
            log.warn("Channel not found: {} ", id);
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        ChannelEntity channelEntity = byId.get();

        if (!channelEntity.getProfileId().equals(profileId)) {
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        channelEntity.setName(dto.getName());
        channelEntity.setDescription(dto.getDescription());

        channelRepository.save(channelEntity);
        return true;
    }


    public Boolean updatePhoto(String id, MultipartFile file, Integer profileId, Language language) {

        String extension = attachService.getExtension(file.getOriginalFilename(), language);

        if (!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg")) {
            throw new FileTypeIncorrectException(resourceBundleService.getMessage("file.type.incorrect", language));
        }

        Optional<ChannelEntity> byId = channelRepository.findById(id);
        if (byId.isEmpty()) { //checking channel is exists
            log.warn("Channel not found: {} ", id);
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        ChannelEntity channelEntity = byId.get();

        if (channelEntity.getProfileId() != profileId) { //checking channel is owned by user
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        AttachResponseDTO attachResponseDTO = attachService.saveToSystem(file, language);
        channelEntity.setPhotoId(attachResponseDTO.getId());
        return true;
    }

    public Boolean updateBanner(String id, MultipartFile file, Integer profileId, Language language) {

        String extension = attachService.getExtension(file.getOriginalFilename(), language);

        if (!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg")) {
            throw new FileTypeIncorrectException(resourceBundleService.getMessage("file.type.incorrect", language));
        }

        Optional<ChannelEntity> byId = channelRepository.findById(id);
        if (byId.isEmpty()) { //checking channel is exists
            log.warn("Channel not found: {} ", id);
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        ChannelEntity channelEntity = byId.get();

        if (channelEntity.getProfileId() != profileId) { //checking channel is owned by user
            log.warn("user: {} have no access to channel: {} ", profileId, id);
            throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
        }

        AttachResponseDTO attachResponseDTO = attachService.saveToSystem(file, language);
        channelEntity.setBannerId(attachResponseDTO.getId());
        return true;
    }

    public Page<ChannelResponseDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ChannelEntity> pageObj = channelRepository.findAll(pageable);

        List<ChannelEntity> content = pageObj.getContent();

        List<ChannelResponseDTO> dtoList = new ArrayList<>();

        for (ChannelEntity channelEntity : content) {
            dtoList.add(toResponseDTO(channelEntity));
        }

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public ChannelResponseDTO getById(String id, Language language) {
        Optional<ChannelEntity> byId = channelRepository.findById(id);
        if (byId.isEmpty()) { //checking channel is exists
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        return toResponseDTO(byId.get());
    }

    public Page<ChannelResponseDTO> getUserChannelsList(Integer page, Integer size, Integer profileId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ChannelEntity> pageObj = channelRepository.findAllByProfileId(profileId, pageable);

        List<ChannelEntity> content = pageObj.getContent();

        List<ChannelResponseDTO> dtoList = new ArrayList<>();

        for (ChannelEntity channelEntity : content) {
            dtoList.add(toResponseDTO(channelEntity));
        }

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public Boolean changeStatus(String id, Integer profileId, Language language) {
        Optional<ChannelEntity> byId = channelRepository.findById(id);
        if (byId.isEmpty()) { //checking channel is exists
            log.warn("Channel not found: {} ", id);
            throw new ChannelNotExistsException(resourceBundleService.getMessage("channel.not.found", language));
        }

        ChannelEntity channelEntity = byId.get();

        ProfileEntity profile = profileRepository.findById(profileId).get();

        if (!channelEntity.getProfileId().equals(profileId)) { //checking channel is owned by user
            if (profile.getRole() != ProfileRole.ROLE_ADMIN) {
                log.warn("user: {} have no access to channel: {} ", profile.getId(), id);
                throw new ChannelAccessDeniedException(resourceBundleService.getMessage("channel.no.access", language));
            }

            if (channelEntity.getStatus() == ChannelStatus.ACTIVE) {
                log.info("{}, Channel status updated to BLOCK", id);

                channelEntity.setStatus(ChannelStatus.BLOCK);
            } else {
                log.info("{}, Channel status updated to ACTIVE", id);

                channelEntity.setStatus(ChannelStatus.ACTIVE);
            }

            return true;
        }


        if (channelEntity.getStatus() == ChannelStatus.ACTIVE) {
            log.info("{}, Channel status updated to BLOCK", id);

            channelEntity.setStatus(ChannelStatus.BLOCK);
        } else {
            log.info("{}, Channel status updated to ACTIVE", id);

            channelEntity.setStatus(ChannelStatus.ACTIVE);
        }

        return true;
    }

    private ChannelResponseDTO toResponseDTO(ChannelEntity channelEntity) {
        ChannelResponseDTO result = new ChannelResponseDTO();
        result.setId(channelEntity.getId());
        result.setName(channelEntity.getName());
        result.setPhotoId(channelEntity.getPhotoId());
        result.setDescription(channelEntity.getDescription());
        result.setStatus(channelEntity.getStatus());
        result.setBannerId(channelEntity.getBannerId());
        result.setProfileId(channelEntity.getProfileId());
        return result;
    }
}
