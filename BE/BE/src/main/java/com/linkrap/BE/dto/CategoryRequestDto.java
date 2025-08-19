package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    @NotBlank(message = "카테고리 이름을 입력하세요.")
    @Size(min = 1, max = 10, message = "카테고리 이름은 1자 이상 10자 이하로 입력해주세요.")
    private String categoryName;
}
