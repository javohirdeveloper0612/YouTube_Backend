package com.example.entity;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String email;
    @Column
    private String password;
    @Column
    // photo type nimadi bolishini bilmadim
    private String photo;
    @Enumerated(value = EnumType.STRING)
    @Column
    private ProfileRole role;

    @Enumerated(value = EnumType.STRING)
    @Column
    private ProfileStatus status;

}
