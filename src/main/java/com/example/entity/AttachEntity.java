package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
@Getter
@Setter

@Entity
@Table(name = "attach")
public class AttachEntity {


//    id(uuid),origin_name,size,type (extension),path,duration
    @Id
    @GenericGenerator(name = "attach_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "original_name")
    private String originalName;
    @Column
    private Long size;
    @Column
    private String path;
    @Column
    private String extension;
    @Column
    private String duration;



}
