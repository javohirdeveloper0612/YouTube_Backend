package com.example.youtube.dto.video;

import com.example.youtube.dto.attach.AttachShortDTO;
import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.dto.category.CategoryShortDTO;
import com.example.youtube.dto.channel.ChannelShortDTO;
import com.example.youtube.dto.tag.TagShortDTO;
import com.example.youtube.enums.VideoStatus;
import com.example.youtube.enums.VideoType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoFullInfo {

    private String id;
    private String title;
    private String description;
    private PreviewAttachDTO previewAttach;
    private AttachShortDTO attach;
    private CategoryShortDTO category;
    private List<TagShortDTO> tagList;
    private LocalDateTime publishedDate;
    private ChannelShortDTO channel;
    private Integer viewCount;
    private Integer sharedCount;
    private Double duration;

    private VideoType type;

    private VideoStatus status;


}
