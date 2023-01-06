package com.example.youtube.dto.category;

import jakarta.persistence.SecondaryTable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryDTO {


    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
