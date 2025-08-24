package com.linkrap.BE.dto;


import com.linkrap.BE.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScrapFavoriteDto {
    private boolean favorite;
    public static ScrapFavoriteDto createScrapFavoriteDto(Scrap scrap){
        return new ScrapFavoriteDto(scrap.isFavorite());
    }
}
