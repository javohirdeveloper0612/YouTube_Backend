package com.example.dto;

import com.example.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChannel {
    private String name;
    private String description;
    private ChannelStatus status;
}
