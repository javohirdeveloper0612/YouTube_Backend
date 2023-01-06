package com.example.youtube.dto.channel;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelInfoDTO {

    private String id;
    private String name;
    private PreviewAttachDTO photo;
}
