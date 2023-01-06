package com.example.youtube.dto.playlistVideo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlaylistVideoResponseDTO {
    private Integer id;
    private Integer playListId;
    private String videoId;
    private LocalDateTime createdDate;
    private Integer orderNum;
}
