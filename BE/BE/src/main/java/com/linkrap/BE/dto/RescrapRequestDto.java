package com.linkrap.BE.dto;

import com.linkrap.BE.entity.ReScrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RescrapRequestDto {
    private Integer userId;
    private Integer scrapId;
    private Integer categoryId;

}
