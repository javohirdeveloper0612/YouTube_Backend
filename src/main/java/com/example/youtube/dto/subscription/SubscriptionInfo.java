package com.example.youtube.dto.subscription;

import com.example.youtube.dto.channel.ChannelInfoDTO;
import com.example.youtube.enums.SubscriptionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionInfo {
   private Integer id;
   private ChannelInfoDTO channel;
   private SubscriptionType type;
   private LocalDateTime dateTime;
}
