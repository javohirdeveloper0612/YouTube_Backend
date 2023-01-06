package com.example.youtube.dto.video;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoShortInfo {
    private String id;
    private String title;
    private PreviewAttachDTO previewAttach;
    private LocalDateTime publishedDate;
    private ChannelShortDTO channel;
    private Integer viewCount;
    private Double duration;
}
