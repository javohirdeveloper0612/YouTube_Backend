package com.example.youtube.mapper.video;



import com.example.youtube.entity.AttachEntity;
import com.example.youtube.entity.ChannelEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VideoShortInfoMapper {
    private String id;
    private String title;
    private AttachEntity previewAttach;
    private LocalDateTime publishedDate;
    private ChannelEntity channel;
    private Integer viewCount;
    private Double duration;
}
