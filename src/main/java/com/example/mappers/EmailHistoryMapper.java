package com.example.mappers;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class EmailHistoryMapper {
    private Integer id;

    @Column
    private String to_email;

    private String title;

    private String message;
    private LocalDateTime createdDate;

    public EmailHistoryMapper(Integer id, String to_email, String title, String message, LocalDateTime createdDate) {
        this.id = id;
        this.to_email = to_email;
        this.title = title;
        this.message = message;
        this.createdDate = createdDate;
    }
    public EmailHistoryMapper( String to_email, String title, String message, LocalDateTime createdDate) {
        this.to_email = to_email;
        this.title = title;
        this.message = message;
        this.createdDate = createdDate;
    }
}
