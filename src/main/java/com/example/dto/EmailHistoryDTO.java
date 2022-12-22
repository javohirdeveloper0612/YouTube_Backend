package com.example.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailHistoryDTO {
    private Integer id;

    @Column
    private String to_email;

    private String title;

    private String message;
    private LocalDateTime createdDate;
}
