package com.example.youtube.entity;

import com.example.youtube.enums.PlaylistStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "playlist")
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "channel_id")
    private String channelId;

    @OneToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "owner_id")
    private Integer ownerId;

    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private ProfileEntity owner;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private PlaylistStatus status;

    @Column
    private String description;

    @Column
    private Integer orderNum;
}
