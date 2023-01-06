package com.example.youtube.dto.video;

import com.example.youtube.dto.profile.ProfileShortDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoSuperDTO {

    private VideoShortInfo videoShortInfo;
    private ProfileShortDTO ownerDTO;

}
