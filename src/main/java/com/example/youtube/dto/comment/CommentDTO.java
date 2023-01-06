package com.example.youtube.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private Integer profileId;
    private String videoId;
    private String content;
    private Integer replyId;
    private Integer likeCount;
    private Integer dislikeCount;
}
