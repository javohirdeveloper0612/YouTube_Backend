package com.example.youtube.dto;

import com.example.youtube.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtDTO {
    private Integer id;

    private String username;
    private ProfileRole role;

    public JwtDTO(String username, ProfileRole role) {
        this.username = username;
        this.role = role;
    }
}
