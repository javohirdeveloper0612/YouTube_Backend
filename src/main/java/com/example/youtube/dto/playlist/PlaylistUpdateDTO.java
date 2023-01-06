package com.example.youtube.dto.playlist;

import com.example.youtube.enums.PlaylistStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistUpdateDTO {
    @NotBlank(message = "Playlist name required")
    private String name;

    @NotNull(message = "Playlist description must be not null ")
    private String description;
}
