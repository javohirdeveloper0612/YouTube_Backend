package com.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")

@Getter
@Setter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;

    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    // 1
    // 1

    @Column(name = "video_id")
    private String videoId;

    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;
    // 1
    // 1

    @Column
    private String content;

    @Column(name = "reply_id")
    private Integer replyId;

    @ManyToOne
    @JoinColumn(name = "reply_id", insertable = false, updatable = false)
    private CommentEntity reply;

    @Column
    private LocalDateTime createdDate;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "dislike_count")
    private Integer dislikeCount = 0;

}
