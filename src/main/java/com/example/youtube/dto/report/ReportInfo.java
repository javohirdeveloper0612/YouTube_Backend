package com.example.youtube.dto.report;


import com.example.youtube.dto.profile.ProfileInfoDTO;
import com.example.youtube.enums.ReposrtType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportInfo {

   private Integer id;
   private ProfileInfoDTO profile;
   private String content;
   private ReposrtType type;
}
