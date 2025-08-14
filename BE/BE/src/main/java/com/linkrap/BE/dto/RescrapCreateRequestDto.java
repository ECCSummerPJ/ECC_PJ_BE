package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Rescrap;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RescrapCreateRequestDto {

    private Integer userId;
    private Integer categoryId;

    public Rescrap toEntity(Scrap scrap) {
        return Rescrap.builder()
                .scrapId(scrap)
                .userId(new Users(userId))
                .categoryId(new Category(categoryId,new Users(userId),null,null,null))
                .redirectLink("/api/scraps/"+scrap.getScrapId())
                .build();
    }
}
