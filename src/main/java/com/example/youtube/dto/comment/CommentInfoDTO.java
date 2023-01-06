package com.example.youtube.dto.comment;

import com.example.youtube.dto.profile.ProfileInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentInfoDTO {
//    id,content,created_date,like_count,dislike_count,
//    profile(id,name,surname,photo(id,url))

    private Integer id;
    private String content;
    private LocalDateTime createdDate;
    private Integer likeCount;
    private Integer dislikeCount;
    private ProfileInfoDTO profile;
}
