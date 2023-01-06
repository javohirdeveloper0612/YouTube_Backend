package com.example.youtube.dto.playlistVideo;

import com.example.youtube.dto.video.VideoShortDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoInfoDTO {
    private Integer playlistId;
    private VideoShortDTO video;

}
