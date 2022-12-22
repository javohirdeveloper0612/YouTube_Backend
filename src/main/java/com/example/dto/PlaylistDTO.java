package com.example.dto;

import com.example.entity.ChannelEntity;
import com.example.enums.PlaylistStatus;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistDTO {
    private Integer id;

    @ManyToOne
    private ChannelEntity channel_id;

    @Column
    private String name;
    private String description;
    private PlaylistStatus status;

    private Long order_num;
}
