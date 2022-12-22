package com.example.entity;

import com.example.enums.PlaylistStatus;
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

    @ManyToOne
    private ChannelEntity channel_id;

    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column
    private PlaylistStatus status;

    @Column
    private Long order_num;
}
