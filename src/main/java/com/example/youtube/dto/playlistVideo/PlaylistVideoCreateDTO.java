package com.example.youtube.dto.playlistVideo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistVideoCreateDTO {
    @NotNull(message = "Playlist Id Required")
    private Integer playlistId;

    @NotBlank(message = "Video Id Required")
    private String videoId;

    @Min(1)
    private Integer orderNum;
}
