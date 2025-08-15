package com.linkrap.BE.service;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.*;
import com.linkrap.BE.repository.CategoryRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.ScrapViewRepository;
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
    @Autowired
    private final ScrapViewRepository scrapViewRepository;

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

        //소유자 id 꺼내기
        Integer ownerId = null;
        if (scrap.getUserId() != null) {            // field 이름이 userId(타입은 Users)
            ownerId = scrap.getUserId().getUserId(); // 진짜 정수 id
        }

        //     일단 소유자의 스크랩이 조회되면 소유자 조회수 1 증가로 기록,나중에 로그인 붙이면 requestUserId == ownerId 일 때만 저장하기
        if (ownerId != null) {
            scrapViewRepository.save(
                    ScrapView.builder()
                            .scrapId(scrap)
                            .userId(ownerId)    // 소유자 id 저장
                            .build()
            );
        }

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
        List<Scrap> scraps=scrapRepository.findByScrapTitleContaining(keyword);
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
