package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class TagResponseDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
