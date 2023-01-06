package com.example.youtube.entity;

import com.example.youtube.enums.SubscriptionStatus;
import com.example.youtube.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "subscription",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "channel_id"}))
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;

    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name ="unsubscribe_date" )
    private LocalDateTime unsubscribeDate;

    @Column
    private SubscriptionStatus status;
    @Column
    private SubscriptionType type;
}
