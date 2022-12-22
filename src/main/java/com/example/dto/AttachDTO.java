package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachDTO {
    private String id;

    private String originalName;

    private Long size;
    private String path;
    private String extension;
    private String duration;
}
