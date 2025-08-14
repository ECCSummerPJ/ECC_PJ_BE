package com.linkrap.BE.service;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Comment;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.CategoryRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {
    @Autowired
    private final ScrapRepository scrapRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UsersRepository usersRepository;

    public ScrapCreateResponseDto create(ScrapDto dto){
        Users user= usersRepository.findById(dto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 생성자가 없습니다."));
        Category category=categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 카테고리가 없습니다."));
        Scrap scrap=Scrap.createScrap(dto,user,category);

        //DB에 저장
        Scrap saved=scrapRepository.save(scrap);
        return new ScrapCreateResponseDto(saved.getScrapId(),saved.getUserIdValue(),saved.getCreatedAt());
    }

    public List<Scrap> index() {
        return scrapRepository.findAll();
    }

    public ScrapShowResponseDto show(Integer scrapId){
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));
        return new ScrapShowResponseDto(scrap.getScrapId(),scrap.getUserIdValue(),scrap.getCategoryIdValue(),scrap.getScrapTitle(),scrap.getScrapLink(),scrap.getScrapMemo(),scrap.isFavorite(),scrap.isShowPublic(),scrap.getCreatedAt(),scrap.getUpdatedAt());
    }

    public ScrapChangeResponseDto update(Integer scrapId, ScrapChangeRequestDto dto){

        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));

        target.patch(dto);
        Scrap updated=scrapRepository.save(target); //db에 저장
        return new ScrapChangeResponseDto(updated.getUpdatedAt());

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

        target.patchFavorite(dto);
        Scrap favorite=scrapRepository.save(target);
        return new ScrapFavoriteDto(favorite.isFavorite());
    }

    @Transactional
    public List<ScrapDto> search(String keyword) {
        List<Scrap> scraps=scrapRepository.findByTitleContaining(keyword);
        List<ScrapDto> scrapDtoList=new ArrayList<>();

        if(scraps.isEmpty()) return scrapDtoList;

        for(Scrap scrap : scraps){
            scrapDtoList.add(this.entityToDto(scrap));
        }

        return scrapDtoList;
    }

    private ScrapDto entityToDto(Scrap scrap) {
        return ScrapDto.builder()
                .scrapId(scrap.getScrapId())
                .userId(scrap.getUserIdValue())
                .categoryId(scrap.getCategoryIdValue())
                .scrapTitle(scrap.getScrapTitle())
                .scrapLink(scrap.getScrapLink())
                .scrapMemo(scrap.getScrapMemo())
                .favorite(scrap.isFavorite())
                .showPublic(scrap.isShowPublic())
                .createdAt(scrap.getCreatedAt())
                .updatedAt(scrap.getUpdatedAt())
                .build();
    }
}
