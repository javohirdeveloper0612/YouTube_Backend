package com.example.service;

import com.example.dto.ChannelDTO;
import com.example.dto.EmailHistoryDTO;
import com.example.entity.ChannelEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.exp.ChannelItemFieldNotFoundException;
import com.example.exp.ItemNotFoundException;
import com.example.mappers.ChannelDTOMapper;
import com.example.mappers.EmailHistoryMapper;
import com.example.repository.ChannelRepository;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    private final ChannelRepository repository ;
    private final ResourceBundleService resourceBundleService ;
    private  final ProfileRepository profileRepository ;

    public ChannelService(ChannelRepository repository,
                          ResourceBundleService resourceBundleService,
                          ProfileRepository profileRepository) {
        this.repository = repository;
        this.resourceBundleService = resourceBundleService;
        this.profileRepository = profileRepository;
    }

    public ChannelDTO create(ChannelDTO dto, Language language) {
        Optional<ChannelEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("we.have.found", language, dto.getId()));
        }

        if (dto.getPhoto().isEmpty()){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("we.have.found", language, dto.getId()));
        }        if (dto.getProfile_id()==null){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("profile_id.not.found", language, dto.getId()));
        }        if (dto.getStatus()==null){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("status.not.found", language, dto.getId()));
        }        if (dto.getDescription().isEmpty()){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("description.not.found", language, dto.getId()));
        }        if (dto.getBanner().isEmpty()){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("banner.not.found", language, dto.getId()));
        }        if (dto.getName().isEmpty()){
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("name.not.found", language, dto.getId()));
        }

        Optional<ProfileEntity> profile = profileRepository.findById(dto.getProfile_id().getId());
        if (profile.isPresent()){
            ProfileEntity entity = profile.get();
            entity.setRole(ProfileRole.ROLE_OWNER);
            profileRepository.save(entity);
        }else {
            throw new ChannelItemFieldNotFoundException(
                    resourceBundleService.getMessage("name.not.found", language, dto.getId())
            );
        }

        ChannelEntity entity = new ChannelEntity() ;
        entity.setBanner(dto.getBanner());
        entity.setName(dto.getName());
        entity.setPhoto(dto.getPhoto());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());
        entity.setProfile_id(dto.getProfile_id());

        repository.save(entity);
        dto.setId(entity.getId());
        return dto ;
    }


    public Boolean update(ChannelDTO dto, Integer id, Language language) {
        Optional<ChannelEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("item.not.found", language, dto.getId()));
        }


        ChannelEntity entity = optional.get();
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        ProfileEntity profile = byId.get();

        if( profile.getRole().equals(ProfileRole.ROLE_ADMIN) || profile.getRole().equals(ProfileRole.ROLE_OWNER)  )
        {
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            repository.save(entity);
            return true ;
        }
        return false ;

    }

    public Boolean updateChannelPhoto(ChannelDTO dto, Integer id, Language language) {
        Optional<ChannelEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("item.not.found", language, dto.getId()));
        }


        ChannelEntity entity = optional.get();
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        ProfileEntity profile = byId.get();

        if( profile.getRole().equals(ProfileRole.ROLE_ADMIN) || profile.getRole().equals(ProfileRole.ROLE_OWNER)  )
        {
            entity.setPhoto(dto.getPhoto());
            repository.save(entity);
            return true ;
        }
        return false ;
    }

    public Boolean updateChannelBanner(ChannelDTO dto, Integer id, Language language) {
        Optional<ChannelEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("item.not.found", language, dto.getId()));
        }


        ChannelEntity entity = optional.get();
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        ProfileEntity profile = byId.get();

        if( profile.getRole().equals(ProfileRole.ROLE_ADMIN) || profile.getRole().equals(ProfileRole.ROLE_OWNER)  )
        {
            entity.setBanner(dto.getBanner());
            repository.save(entity);
            return true ;
        }
        return false ;
    }

    public Page<ChannelDTO> getPageList(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ChannelDTOMapper> pageObj = repository.getList(pageable );

        List<ChannelDTO> dtoList = new ArrayList<>();

        for (ChannelDTOMapper mapper : pageObj) {
            ChannelDTO dto = new ChannelDTO();
            dto.setName(mapper.getName());
            dto.setDescription(mapper.getDescription());
            dto.setBanner(mapper.getBanner());
            dto.setPhoto(mapper.getPhoto());
            dtoList.add(dto);
        }


        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());


    }

    public ChannelDTO findById(String id , Integer profile_Id, Language language) {

        Optional<ProfileEntity> optional = profileRepository.findById(profile_Id);

        if (optional.isEmpty()){
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("item.not.found", language ));
        }

        Optional<ChannelEntity> entityOptional = repository.findById(id);

        ChannelEntity entity = entityOptional.get();
        ChannelDTO dto = new ChannelDTO() ;
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setBanner(entity.getBanner());
        dto.setPhoto(entity.getPhoto());
        return dto  ;
    }

    public Boolean updateChannelStatus(ChannelDTO dto, Integer id, Language language) {

        Optional<ChannelEntity> optional = repository.findById(dto.getId());

        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    resourceBundleService.getMessage("item.not.found", language, dto.getId()));
        }


        ChannelEntity entity = optional.get();
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        ProfileEntity profile = byId.get();

        if( profile.getRole().equals(ProfileRole.ROLE_ADMIN) || profile.getRole().equals(ProfileRole.ROLE_OWNER)  )
        {
            entity.setStatus(dto.getStatus());
            repository.save(entity);
            return true ;
        }
        return false ;
    }

}
