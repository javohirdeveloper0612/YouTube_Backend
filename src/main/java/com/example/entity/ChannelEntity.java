package com.example.entity;

import com.example.enums.ChannelStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter

@Entity
@Table(name = "channel")
public class ChannelEntity {


    @Id
    @GenericGenerator(name = "attach_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    private String name;

    @Column
    private String photo;

    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column
    private ChannelStatus status;

    @Column
    private String banner;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile_id;
}
