package com.example.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "playlist_video",
        uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "video_id"}))
public class PlaylistVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlist_id")
    private Integer playlistId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private PlaylistEntity playlist;
    // 1
    // 1

    @Column(name = "video_id")
    private String videoId;

    @OneToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;
    //1 1
    //1 X(VIDEO UNIQUE BO'LISHI KERAK)

    @Column
    private LocalDateTime createdDate;

    @Column
    private Integer orderNum;
}
