package com.example.dto;

import com.example.entity.ProfileEntity;
import com.example.enums.ChannelStatus;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDTO {
    private String id;


    private String name;

    private String photo;

    private String description;

    private ChannelStatus status;

    private String banner;

    private ProfileEntity profile_id;
}
