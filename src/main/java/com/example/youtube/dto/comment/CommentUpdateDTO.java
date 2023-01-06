package com.example.youtube.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDTO {
    @NotBlank(message = "Video Id Required")
    private String videoId;

    @NotBlank(message = "Content Required")
    private String content;
}
