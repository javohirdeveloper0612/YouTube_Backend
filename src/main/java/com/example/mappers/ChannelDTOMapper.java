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

    private String banner;

    public ChannelDTOMapper(String name, String photo, String description, String banner) {
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.banner = banner;
    }
}
