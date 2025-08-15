package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    private String categoryName;

}
