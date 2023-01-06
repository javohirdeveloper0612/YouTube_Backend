package com.example.youtube.dto.tag;

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
