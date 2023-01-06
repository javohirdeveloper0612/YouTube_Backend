package com.example.youtube.dto.videoLike;

import com.example.youtube.enums.LikeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VideoLikeResponseDTO {
    private Integer id;
    private Integer profileId;
    private String videoId;
    private LocalDateTime createdDate;
    private LikeType likeType;
}
