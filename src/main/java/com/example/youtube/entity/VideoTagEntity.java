package com.example.youtube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.text.html.HTML;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_tag")

@Getter
@Setter
public class VideoTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "video_id")
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;


    @Column(name = "tag_id")
    private Integer tagId;
    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagEntity tag;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
}

