package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScrapListDto {
   private Integer scrapId;
   private String scrapTitle;
   private String scrapLink;
   private String scrapMemo;
   private boolean favorite;
   private boolean showPublic;

   public static ScrapListDto createScrapListDto(Scrap scraps) {
       return new ScrapListDto(
               scraps.getScrapId(),
               scraps.getScrapTitle(),
               scraps.getScrapLink(),
               scraps.getScrapMemo(),
               scraps.isFavorite(),
               scraps.isShowPublic()
       );
   }

}