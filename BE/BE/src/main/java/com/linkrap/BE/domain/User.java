package com.linkrap.BE.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_users_user_id", columnNames = "user_id"),
                @UniqueConstraint(name="uk_users_email", columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, length = 20)
    private String userId;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false,unique = true, length = 100)
    private String email;

    @Column(name="password_hash", nullable = false, length = 100)
    private String passwordHash;

    //@Lob
    //@Column(name = "profile_image")
    //private byte[] profileImage;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
