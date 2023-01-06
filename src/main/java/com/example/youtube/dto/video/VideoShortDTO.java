package com.example.youtube.dto.video;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoShortDTO {
    private String id;
    private String title;
    private ChannelShortInfoDTO channel;
    private Double duration;
    private PreviewAttachDTO previewAttach;
}
