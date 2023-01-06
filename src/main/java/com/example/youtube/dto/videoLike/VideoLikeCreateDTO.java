package com.example.youtube.dto.videoLike;

import com.example.youtube.enums.LikeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoLikeCreateDTO {
    @NotBlank(message = "Video id required")
    private String videoId;

    @NotNull(message = "Video id required")
    private LikeType likeType;
}
