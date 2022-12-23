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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository repository ;
    @Autowired
    private ResourceBundleService resourceBundleService ;
    @Autowired
    private ProfileRepository profileRepository ;

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
            entity.setStatus(dto.getStatus());
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

//        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<ChannelDTOMapper> pageObj = repository.getList(pageable );
//
//        List<EmailHistoryDTO> dtoList = new ArrayList<>();
//
//        for (EmailHistoryMapper emailHistoryMapper : pageObj) {
//            EmailHistoryDTO dto = new EmailHistoryDTO();
//            dto.setId(emailHistoryMapper.getId());
//            dto.setTo_email(emailHistoryMapper.getTo_email());
//            dto.setTitle(emailHistoryMapper.getTitle());
//            dto.setMessage(emailHistoryMapper.getMessage());
//            dto.setCreatedDate(emailHistoryMapper.getCreatedDate());
//            dtoList.add(dto) ;
//        }
//
//
//        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
        return  null ;

    }
}
