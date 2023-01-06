package com.example.youtube.dto.profile;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.enums.ReposrtType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileInfoDTO {
    private Integer id;
    private String name;
    private String surname;
    private PreviewAttachDTO photo;

}
