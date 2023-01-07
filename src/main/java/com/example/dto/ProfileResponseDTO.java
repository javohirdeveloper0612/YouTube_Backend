package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProfileResponseDTO {
    private Integer id;
    private String name;
    private String surname;

    private String email;

    private boolean visible;
    private ProfileStatus status;
    private ProfileRole role;
    private LocalDateTime createdDate;
    private String photoId;

    public ProfileResponseDTO(Integer id, String name, String surname, ProfileStatus status, ProfileRole role, Boolean visible, LocalDateTime createdDate) {
    }


    public ProfileResponseDTO(Integer id, String name, String surname, ProfileStatus status, ProfileRole role, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.role = role;
        this.createdDate = createdDate;
    }


}
