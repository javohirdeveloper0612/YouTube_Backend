package com.example.youtube.dto;

import com.example.youtube.entity.ChannelEntity;
import com.example.youtube.entity.ProfileEntity;
import com.example.youtube.enums.ReposrtType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {

    private Integer id;

    private Integer profileId;

    private ProfileEntity profile;

    private String content;

    private String channleId;

    private ChannelEntity channel;

    private ReposrtType type;
}
