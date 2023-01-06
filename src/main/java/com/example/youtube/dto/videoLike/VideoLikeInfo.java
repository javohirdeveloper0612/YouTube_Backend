package com.example.youtube.dto.videoLike;

import com.example.youtube.dto.video.VideoFullInfo;
import com.example.youtube.dto.video.VideoShortDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoLikeInfo {
    private Integer id;
    private VideoShortDTO videoShortInfo;
}
