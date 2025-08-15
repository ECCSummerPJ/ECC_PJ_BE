package com.linkrap.BE.entity;

import com.linkrap.BE.dto.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;
    @Column
    private String categoryName;
    @Column
    @CreatedDate
    private Timestamp createdAt;
    @Column
    @LastModifiedDate
    private Timestamp updatedAt;

    public static Category createCategory(Users user, CategoryRequestDto dto) {
        return Category.builder()
                .user(user)
                .categoryName(dto.getCategoryName())
                .build();
    }

    public void patch(CategoryRequestDto dto) {
        if (dto.getCategoryName() != null)
            this.categoryName = dto.getCategoryName();
    }
}
