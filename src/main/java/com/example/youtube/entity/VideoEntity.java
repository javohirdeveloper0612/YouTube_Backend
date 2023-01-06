package com.example.youtube.entity;

import com.example.youtube.enums.VideoStatus;
import com.example.youtube.enums.VideoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;


@Getter
@Setter

@Entity
@Table(name = "video")
public class VideoEntity {

    @Id
    @GeneratedValue(generator = "video_uuid")
    @GenericGenerator(name = "video_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @OneToOne
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;


    @Column
    private String title;


    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;


    @Column(name = "attach_id")
    private String attachId;
    @OneToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();


    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column
    private VideoStatus status;


    @Enumerated(EnumType.STRING)
    @Column
    private VideoType type;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @Column
    private String description;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;


    @Column(name = "like_count")
    private Integer likeCount = 0;


    @Column(name = "dislike_count")
    private Integer dislikeCount = 0;


    @Column(name = "owner_id")
    private Integer ownerId;
    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private ProfileEntity owner;

    @Column
    private Double duration;


}
