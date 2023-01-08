package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateDTO {
    @NotBlank(message = "Name required")
    private String name;
}
