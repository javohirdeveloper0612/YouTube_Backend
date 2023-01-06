package com.example.youtube.dto.auth;

import com.example.youtube.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private String name;
    private String surname;
    private ProfileRole role;
    private String token;

}
