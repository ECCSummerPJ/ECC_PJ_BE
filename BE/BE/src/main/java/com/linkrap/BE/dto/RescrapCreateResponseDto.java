package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RescrapCreateResponseDto {

    private Integer scrapId;
    private Integer rescrapId;
    private String redirectLink;
    private Timestamp createdAt;
}
