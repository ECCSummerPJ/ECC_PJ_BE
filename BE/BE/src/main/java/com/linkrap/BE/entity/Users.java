package com.linkrap.BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String profileImage;

    @Column
    private String nickname;
}

