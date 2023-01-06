package com.example.youtube.dto.report;

import com.example.youtube.dto.attach.PreviewAttachDTO;
import com.example.youtube.entity.AttachEntity;
import com.example.youtube.enums.ReposrtType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileReportDTO {

    private  Integer id;
    private  String name;
    private  String surname;
    private PreviewAttachDTO photo;
    private ReposrtType type;

}
