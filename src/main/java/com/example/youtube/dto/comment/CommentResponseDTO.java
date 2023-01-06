package com.example.youtube.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {
    private Integer id;
    private Integer profileId;
    private String videoId;
    private String content;
}
