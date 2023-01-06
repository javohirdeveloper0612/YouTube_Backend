package com.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_watched", uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "video_id"}))

@Getter
@Setter
public class VideoWatchedEntity {

    @Id
    @GeneratedValue(generator = "video_watched_uuid")
    @GenericGenerator(name = "video_watched_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;


    @Column(name = "video_id")
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;


    @Column(name = "created_date")
    private LocalDateTime createdDate;


}
