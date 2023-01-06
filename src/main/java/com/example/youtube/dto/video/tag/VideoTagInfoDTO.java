package com.example.youtube.dto.video.tag;


import com.example.youtube.dto.tag.TagShortDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class VideoTagInfoDTO {

    private Integer id;
    private String videoId;
    private TagShortDTO tagShortDTO;
    private LocalDateTime createdDate;

}
