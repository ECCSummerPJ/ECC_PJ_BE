package com.linkrap.BE.entity;

import com.linkrap.BE.dto.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Table(name="category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "category_name"})
}) //카테고리 이름 중복 제한
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
    @Column(updatable = false)
    @CreatedDate
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }


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
