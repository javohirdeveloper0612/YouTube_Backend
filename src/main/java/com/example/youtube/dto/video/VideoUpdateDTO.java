package com.example.youtube.dto.video;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VideoUpdateDTO {

    private String id;
    private String title;
    private String description;
    private String previewAttachId;
    private Integer categoryId;

}
