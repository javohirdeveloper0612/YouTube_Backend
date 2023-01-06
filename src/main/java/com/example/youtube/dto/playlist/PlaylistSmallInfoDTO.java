package com.example.youtube.dto.playlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistSmallInfoDTO {
    private Integer id;
    private String name;
    private Integer videoCount; // nechta video bor
    private Integer videoTotalCount; // ichidagi videolarni ko'rilganlar soni
//    private LocalDate lastUpdateDate; // ???



}
