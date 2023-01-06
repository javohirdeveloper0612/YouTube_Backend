package com.example.youtube.dto.playlist;

import com.example.youtube.dto.channel.ChannelShortInfoDTO;
import com.example.youtube.dto.video.VideoSmallInfoDTO;
import com.example.youtube.enums.PlaylistStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayListShortInfoDTO {
    private Integer id;
    private ChannelShortInfoDTO channel;
    private Integer videoCount;
    private List<VideoSmallInfoDTO> videoList;
    private PlaylistStatus status;
    private Integer orderNum;
}
