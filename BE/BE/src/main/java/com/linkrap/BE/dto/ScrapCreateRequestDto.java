package com.linkrap.BE.dto;

import lombok.*;

@AllArgsConstructor //모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor //매개변수가 아예 없는 기본 생성자 자동 생성
@Getter //각 필드 값을 조회할 수 있는 getter 메서드 자동 생성
@ToString //모든 필드를 출력할 수 있는 toString 메서드 자동 생성
@Builder
public class ScrapCreateRequestDto {

    private String categoryName;
    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean showPublic;
}
