package com.example.youtube.dto.subscription;

import com.example.youtube.entity.ChannelEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.enums.SubscriptionStatus;
import com.example.youtube.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionDTO {

    private Integer id;


    private Integer profileId;

    private ProfileEntity profile;

    private String channelId;

    private ChannelEntity channel;

    private LocalDateTime createdDate;

    private LocalDateTime unsubscribeDate;

    private SubscriptionStatus status;

    private SubscriptionType type;
}
