package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Scrap;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor //모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor //매개변수가 아예 없는 기본 생성자 자동 생성
@Getter //각 필드 값을 조회할 수 있는 getter 메서드 자동 생성
@ToString //모든 필드를 출력할 수 있는 toString 메서드 자동 생성
@Builder
public class ScrapDto {
    private Integer scrapId;
    private Integer userId;
    private Integer categoryId;
    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean favorite;
    private boolean showPublic;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static ScrapDto createScrapDto(Scrap scrap) {
        return new ScrapDto(
                scrap.getScrapId(),
                scrap.getUserIdValue(),
                scrap.getCategoryIdValue(),
                scrap.getScrapTitle(),
                scrap.getScrapLink(),
                scrap.getScrapMemo(),
                scrap.isFavorite(),
                scrap.isShowPublic(),
                scrap.getCreatedAt(),
                scrap.getUpdatedAt()
        );
    }

}
