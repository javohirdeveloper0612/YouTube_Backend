package com.example.youtube.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class EmailHistoryResponseDTO {
    private Integer id;
    private String email;
    private String message;
    private LocalDateTime createdDate;

}
