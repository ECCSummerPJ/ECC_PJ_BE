package com.linkrap.BE.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    @Size(min = 1, max = 10, message = "카테고리 이름은 1자 이상 10자 이하로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]*$", message = "카테고리 이름은 한글, 영어, 숫자만 포함할 수 있습니다.")
    private String categoryName;
}
