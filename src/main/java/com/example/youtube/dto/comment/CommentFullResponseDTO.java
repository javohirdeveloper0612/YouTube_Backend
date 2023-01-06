package com.example.youtube.dto.comment;

import com.example.youtube.dto.video.VideoExtraShortDTO;
import com.example.youtube.dto.video.VideoFullInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentFullResponseDTO {
    private Integer id;
    private String content;
    private Integer replyId;
    private Integer likeCount;
    private Integer dislikeCount;
    private VideoExtraShortDTO video;
}
