package com.linkrap.BE.service;

import com.linkrap.BE.dto.RescrapCreateResponseDto;
import com.linkrap.BE.dto.RescrapDto;
import com.linkrap.BE.dto.RescrapShowResponseDto;
import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Rescrap;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.CategoryRepository;
import com.linkrap.BE.repository.RescrapRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescrapService {
    @Autowired
    private final ScrapRepository scrapRepository;
    @Autowired
    private final RescrapRepository rescrapRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UsersRepository usersRepository;

    public RescrapCreateResponseDto create(Integer scrapId, Integer userId, RescrapDto dto) {

        //1. 동일한 scrapId 가진 scrap 찾아옴
        Scrap scrap=scrapRepository.findById(scrapId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! "+"대상 게시글이 없습니다."));
        Users user= usersRepository.findById(dto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 생성자가 없습니다."));
        Category category=categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 카테고리가 없습니다."));
        if (!dto.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인 스크랩은 리스크랩하지 못합니다.");
        }
        //2. dto->엔티티 변환
        Rescrap rescrap=Rescrap.createRescrap(dto,scrap,user,category);

        //rescrap DB에 저장
        Rescrap saved=rescrapRepository.save(rescrap);

        return new RescrapCreateResponseDto(saved.getScrapIdValue(),saved.getRescrapId(),saved.getRedirectLink(),saved.getCreatedAt());
    }

    public RescrapShowResponseDto show(Integer rescrapId) {
        Rescrap rescrap = rescrapRepository.findById(rescrapId).orElseThrow(()->new NoSuchElementException("RESCRAP_NOT_FOUND: "+rescrapId));
        return new RescrapShowResponseDto(rescrap.getRedirectLink());
    }

    public RescrapDto delete(Integer rescrapId, Integer userId) {
        //1. 리스크랩 조회 및 예외 발생
        Rescrap target=rescrapRepository.findById(rescrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+rescrapId));
        if (!target.getUserIdValue().equals(userId)) {
            throw new IllegalArgumentException("본인 리스크랩만 삭제할 수 있습니다.");
        }
        //2. 리스크랩 삭제
        rescrapRepository.delete(target);
        //3. 삭제 리스크랩을 DTO로 변환 및 반환
        return RescrapDto.createRescrapDto(target);
    }
}
