package com.example.mappers;

import com.example.enums.ChannelStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelDTOMapper {
    private String name;

    private String photo;

    private String description;

    private ChannelStatus status;

    private String banner;

}
