package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    private Integer id;

    private String name;

    private String surname;

    private String email;
    private String password;
    // photo type nimadi bolishini bilmadim
    private String photo;

    private ProfileRole role;


    private ProfileStatus status;
}
