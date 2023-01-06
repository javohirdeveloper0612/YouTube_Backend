package com.example.youtube.dto.video.tag;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VideoTagDTO {

    @NotBlank
    private String videoId;

    @Min(value = 1)
    private Integer tagId;
}
