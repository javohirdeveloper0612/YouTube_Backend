package com.example.youtube.dto.video;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoExtraShortDTO {
    private String id;
    private String title;
    private Double duration;
    private PreviewAttachDTO previewAttach;
}
