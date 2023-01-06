package com.example.youtube.service;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelInfoDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import com.example.youtube.dto.subscription.SubscriptionDTO;
import com.example.youtube.dto.subscription.SubscriptionInfo;
import com.example.youtube.entity.AttachEntity;
import com.example.youtube.entity.ChannelEntity;
import com.example.youtube.entity.SubscriptionEntity;
import com.example.youtube.enums.Language;
import com.example.youtube.enums.SubscriptionStatus;
import com.example.youtube.enums.SubscriptionType;
import com.example.youtube.exp.SubscriptionNotFoud;
import com.example.youtube.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {


    @Autowired
    private ResourceBundleService resourceBundleService;

    @Value("${attach.download.url}")
    private String attachDownloadUrl;


    @Autowired
    private SubscriptionRepository repo;

    public String add(SubscriptionDTO dto, Language language) {


    SubscriptionEntity entity=new SubscriptionEntity();
    entity.setId(dto.getId());
    entity.setChannelId(dto.getChannelId());
    entity.setType(dto.getType());
    repo.save(entity);

        return "Added";
    }

    public Boolean changeStatus(Integer id,Language language) {


        Optional<SubscriptionEntity> entityList = repo.findByChannelId(id);

        if (entityList.isEmpty()) {
            throw new SubscriptionNotFoud(resourceBundleService.getMessage("subscription.Not.found", language));
        }

        SubscriptionEntity entity = entityList.get();
        if (entity.getStatus() == SubscriptionStatus.ACTIIVE) {
            entity.setStatus(SubscriptionStatus.BLOCK);
        } else {
            entity.setStatus(SubscriptionStatus.ACTIIVE);
        }

        repo.save(entity);

        return true;
    }

    public Boolean changeNotificationType(Integer id,Language language){
        Optional<SubscriptionEntity> entity=repo.findByChannelId(id);
        if (entity.isEmpty()){
           throw new SubscriptionNotFoud(resourceBundleService.getMessage("subscription.Not.found", language));
        }
        SubscriptionEntity entity1= entity.get();
        if (entity1.getType()== SubscriptionType.ALL){
            entity1.setType(SubscriptionType.NON);
        }else {
            entity1.setType(SubscriptionType.ALL);
        }
        repo.save(entity1);
        return true;
    }




    public List<SubscriptionInfo> getUserSubscrition(Integer pid) {
        List<SubscriptionInfo> subscription = new LinkedList<>();

        List<SubscriptionEntity> info = repo.findByProfileId(pid);

        for (SubscriptionEntity i : info) {
            subscription.add(getSubsriptionInfo(i));
        }
        return subscription;
    }

    public List<SubscriptionInfo> getUserSubscrition2(Integer id) {
        List<SubscriptionInfo> subscription = new LinkedList<>();

        List<SubscriptionEntity> info = repo.findByProfileId(id);

        for (SubscriptionEntity i : info) {
            subscription.add(getSubsriptionInfo(i));
        }
        return subscription;
    }




    private SubscriptionInfo getSubsriptionInfo(SubscriptionEntity entity) {
        SubscriptionInfo info = new SubscriptionInfo();
        info.setId(entity.getId());
        info.setChannel(getChannelInfo(entity.getChannel()));
        info.setType(entity.getType());
        info.setDateTime(entity.getCreatedDate());


        return info;

    }


    private PreviewAttachDTO getPhoto(AttachEntity entity) {

        PreviewAttachDTO preview = new PreviewAttachDTO();
        preview.setId(entity.getId());
        preview.setUrl(attachDownloadUrl + entity.getId() + "." + entity.getType());

        return preview;
    }


    private ChannelInfoDTO getChannelInfo(ChannelEntity entity) {
        ChannelInfoDTO dto = new ChannelInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhoto(getPhoto(entity.getPhoto()));


        return dto;
    }

}
