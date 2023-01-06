package com.example.youtube.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelUpdatePropertiesDTO {
    @NotBlank(message = "Name required")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "Name required")
    private String description;
}
