package com.linkrap.BE.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //로그인용 ID
    @Column(name = "USER_ID", nullable = false, unique = true, length = 20)
    private String userId;

    @Column(name = "NICKNAME", nullable = false, length = 100)
    private String nickname;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "EMAIL", nullable = false,unique = true, length = 100)
    private String email;

    @Column(name = "PROFILE_IMAGE_URL", length = 500)
    private String profileImageUrl;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        var now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
