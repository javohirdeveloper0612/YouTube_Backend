package com.example.youtube.dto.video.tag;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoTagResponseDTO {

    private String videoId;
    private Integer tagId;
    private LocalDateTime createdDate;
}
