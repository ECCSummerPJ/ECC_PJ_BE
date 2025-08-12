package com.linkrap.BE.service;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.ReScrap;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {
    @Autowired
    private final ScrapRepository scrapRepository;

    public ScrapCreateResponseDto create(ScrapCreateRequestDto dto){
        //dto->entity
        Scrap scrap=dto.toEntity();
        if(scrap.getScrapId()!=null)
            return null;
        //DB에 저장
        Scrap saved=scrapRepository.save(scrap);
        return new ScrapCreateResponseDto(saved.getScrapId(),saved.getAuthorId(),saved.getCreatedAt());
    }


    public Scrap show(Integer scrapId){
        return scrapRepository.findById(scrapId).orElse(null);
    }

    public ScrapChangeResponseDto update(Integer scrapId, ScrapChangeRequestDto dto){

        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));

        target.patch(dto);
        Scrap updated=scrapRepository.save(target); //db에 저장
        return new ScrapChangeResponseDto(target.getUpdatedAt());

    }

    public Scrap delete(Integer scrapId) {
        Scrap target=scrapRepository.findById(scrapId).orElse(null);
        if(target==null)
            return null;

        scrapRepository.delete(target);
        return target;
    }

    public ScrapFavoriteDto favorite(Integer scrapId, ScrapFavoriteDto dto) {
        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));

        target.patchFavortite(dto);
        Scrap favorite=scrapRepository.save(target);
        return new ScrapFavoriteDto(target.isFavorite());
    }

    /*
    public RescrapRequestDto rescrap(Integer scrapId, RescrapRequestDto dto) {

    }
     */
}
