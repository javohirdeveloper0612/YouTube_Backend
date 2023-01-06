package com.example.youtube.dto.video;


import com.example.youtube.dto.tag.TagShortDTO;
import com.example.youtube.enums.VideoType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class VideoCreateDTO {

    @NotBlank
    private String title;


    private String description;


    private String previewAttachId;

    @NotBlank
    private String attachId;

    @Min(value = 0)
    private Integer categoryId;


    @NotBlank
    private String channelId;

    @Min(value = 1)
    private Double duration;

    @NotNull
    private VideoType type;

}
